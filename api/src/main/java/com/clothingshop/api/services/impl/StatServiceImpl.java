package com.clothingshop.api.services.impl;

import com.clothingshop.api.domain.dtos.OrderGroupByIntervalDto;
import com.clothingshop.api.domain.dtos.OrderStatResponseDto;
import com.clothingshop.api.domain.entities.OrderTimeEntity;

import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.domain.enums.Interval;
import com.clothingshop.api.domain.seed.SeedData;
import com.clothingshop.api.repositories.CategoryRepository;
import com.clothingshop.api.repositories.ProductRepository;
import com.clothingshop.api.repositories.UserRepository;
import com.clothingshop.api.services.StatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Log
@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public List<OrderStatResponseDto> getOrderStat(Date fromDate, Date toDate, String timeZone, Long userId,
            Interval intervalType) {
        List<OrderGroupByIntervalDto> orderGroupByIntervalDtos = getOrderGroupByIntervalDtos(fromDate, toDate, timeZone,
                userId, intervalType);

        LocalDateTime fromLocalDateTime = getTruncatedLocalDateTime(fromDate, timeZone, intervalType);
        LocalDateTime toLocalDateTime = getTruncatedLocalDateTime(toDate, timeZone, intervalType);

        int maxNumEntries = (int) intervalType.getChronoUnit().between(fromLocalDateTime, toLocalDateTime) + 1;
        log.info("fromLocalDateTime " + fromLocalDateTime);
        log.info("toLocalDateTime " + toLocalDateTime);

        ArrayList<OrderStatResponseDto> orderStatResponseDtos = new ArrayList<>(maxNumEntries);

        DateTimeFormatter toStringFormatter = DateTimeFormatter.ofPattern(intervalType.getToStringFormat());
        String toLocalDateTimeFormatter = intervalType.getToLocalTimeFormat();

        for (OrderGroupByIntervalDto order : orderGroupByIntervalDtos) {
            LocalDateTime currentLocalDateTime = LocalDateTime.parse(order.get_id() + toLocalDateTimeFormatter);
            log.info("interval " + currentLocalDateTime);

            while (fromLocalDateTime.isBefore(currentLocalDateTime)) {
                orderStatResponseDtos.add(new OrderStatResponseDto(fromLocalDateTime.format(toStringFormatter), 0));
                fromLocalDateTime = fromLocalDateTime.plus(1, intervalType.getChronoUnit());
            }

            orderStatResponseDtos
                    .add(new OrderStatResponseDto(currentLocalDateTime.format(toStringFormatter), order.getCount()));

            fromLocalDateTime = fromLocalDateTime.plus(1, intervalType.getChronoUnit());
        }
        log.info("fromLocalDateTime " + fromLocalDateTime);
        while (!fromLocalDateTime.isAfter(toLocalDateTime)) {
            orderStatResponseDtos.add(new OrderStatResponseDto(fromLocalDateTime.format(toStringFormatter), 0));
            fromLocalDateTime = fromLocalDateTime.plus(1, intervalType.getChronoUnit());
        }

        return orderStatResponseDtos;
    }

    List<OrderGroupByIntervalDto> getOrderGroupByIntervalDtos(Date fromDate, Date toDate, String timeZone,
            Long userId, Interval intervalType) {
        // Define the aggregation pipeline
        String intervalName = intervalType.toString();

        Criteria criteria = Criteria.where("createdAt").gte(fromDate).lte(toDate);

        if (userId != null) {
            criteria.and("userId").is(userId);
        }

        Aggregation aggregation = newAggregation(
                match(criteria),
                project().and("createdAt").as("createdAt")
                        .andExpression("{ $dateToString: { format: '" + intervalType.getDatabaseTimeFormat()
                                + "', date: '$createdAt', timezone:'" + timeZone + "' } }")
                        .as(intervalName),
                group(intervalName).count().as("count"),
                sort(Direction.ASC, "_id"));

        AggregationResults<OrderGroupByIntervalDto> result = mongoTemplate.aggregate(
                aggregation, "orders", OrderGroupByIntervalDto.class);

        return result.getMappedResults();
    }

    LocalDateTime getTruncatedLocalDateTime(Date fromDate, String timeZone, Interval intervalType) {
        LocalDateTime untruncatedLocalDateTime = fromDate.toInstant().atZone(ZoneId.of(timeZone)).toLocalDateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(intervalType.getTruncatedLocalDateTimeFormat());
        String truncatedLocalDateTimeString = untruncatedLocalDateTime.format(dateTimeFormatter);
        return LocalDateTime.parse(truncatedLocalDateTimeString);
    }

    @Transactional
    public void seed() throws Exception {
        String seedDataJson = new String(
                Files.readAllBytes(Paths.get("src/main/java/com/clothingshop/api/domain/seed/seed.json")));
        ObjectMapper objectMapper = new ObjectMapper();
        SeedData seedData = objectMapper.readValue(seedDataJson, SeedData.class);

        categoryRepository.saveAll(Arrays.stream(seedData.getCategories()).toList());

        productRepository.saveAll(Arrays.stream(seedData.getProducts()).toList());

        List<UserEntity> hashPasswordUsers = Arrays.stream(seedData.getUsers()).peek(user -> {
            user.setPassword(encoder.encode(user.getPassword()));
        }).toList();

        userRepository.saveAll(hashPasswordUsers);

        List<OrderTimeEntity> orderTimeEntities = getOrderTimes();

        mongoTemplate.insert(orderTimeEntities, OrderTimeEntity.class);
    }

    List<OrderTimeEntity> getOrderTimes() {
        LocalDateTime toDate = LocalDateTime.now().minusHours(1);
        LocalDateTime fromDate = toDate.minusYears(1);
        // truncated month
        fromDate = LocalDateTime.of(fromDate.getYear(), fromDate.getMonth(), 1, 0, 0);
        LocalDateTime preFromDate = fromDate.minusHours(1);

        double orderedMonthProb = 0.75;
        double orderedDaysOfMonthProb = 0.6;
        double orderedHourProb = 0.5;
        int maxOrder = 10;
        int minOrder = 1;
        int rangeOrder = maxOrder - minOrder + 1;
        int maxUserIndex = 1000;
        int minUserIndex = 0;
        int rangeUserIndex = maxUserIndex - minUserIndex + 1;
        long orderId = 1;
        int numRecord = 12 * 31 * 24 * 10;
        ArrayList<OrderTimeEntity> orderTimes = new ArrayList<>((int) numRecord);

        while (!fromDate.isAfter(toDate)) {
            if (preFromDate.getMonth() != fromDate.getMonth()) {
                // new month
                if (Math.random() > orderedMonthProb) {
                    fromDate = fromDate.plusMonths(1);
                    preFromDate = fromDate.minusHours(1);
                    continue;
                }
            }

            if (preFromDate.getDayOfMonth() != fromDate.getDayOfMonth()) {
                // new day of month
                if (Math.random() > orderedDaysOfMonthProb) {
                    fromDate = fromDate.plusDays(1);
                    preFromDate = fromDate.minusHours(1);
                    continue;
                }
            }

            if (Math.random() < orderedHourProb) {
                int numOrder = (int) (Math.random() * rangeOrder) + minOrder;
                Date curr = Date
                        .from(fromDate.atZone(ZoneId.systemDefault())
                                .toInstant());
                for (int l = 0; l < numOrder; ++l) {
                    int userIndex = (int) (Math.random() * rangeUserIndex) + minUserIndex;
                    OrderTimeEntity newOrder = OrderTimeEntity.builder().createdAt(curr)
                            .userId((long) userIndex).orderId(orderId)
                            .build();
                    orderTimes.add(newOrder);
                    ++orderId;
                }
            }

            preFromDate = fromDate;
            fromDate = fromDate.plusHours(1);
        }

        return orderTimes;
    }
}

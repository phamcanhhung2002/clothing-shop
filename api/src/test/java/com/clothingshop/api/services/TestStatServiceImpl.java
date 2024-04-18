package com.clothingshop.api.services;

import com.clothingshop.api.domain.ExpectData;
import com.clothingshop.api.domain.MockData;
import com.clothingshop.api.domain.dtos.OrderStatResponseDto;
import com.clothingshop.api.domain.entities.OrderTimeEntity;
import com.clothingshop.api.domain.enums.Interval;
import com.clothingshop.api.services.impl.StatServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestStatServiceImpl {

    @Autowired
    StatServiceImpl underTest;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void testThatGetOrderStatReturnCorrectResult() throws IOException, ParseException {
        // Prepare data
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        ObjectMapper objectMapper = new ObjectMapper();

        String mockDataJson = new String(
                Files.readAllBytes(Paths.get("src/test/java/com/clothingshop/api/mock-data/input.json")));
        MockData mockData = objectMapper.readValue(mockDataJson, MockData.class);

        String expectDataJson = new String(
                Files.readAllBytes(Paths.get("src/test/java/com/clothingshop/api/mock-data/expect.json")));
        ExpectData expectData = objectMapper.readValue(expectDataJson, ExpectData.class);

        List<OrderTimeEntity> orderTimeEntities = Arrays.stream(mockData.getInput()).map(mock -> {
            try {
                return OrderTimeEntity.builder().orderId(mock.getOrderId())
                        .userId(mock.getUserId()).createdAt(isoFormat.parse(mock.getCreatedAt())).build();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        mongoTemplate.insert(orderTimeEntities, OrderTimeEntity.class);

        String timeZone = "+07:00";
        Long userId = null;
        Interval interval = Interval.day;
        Date fromDate = isoFormat.parse("2024-04-15T00:00:00.000+07:00");
        Date toDate = isoFormat.parse("2024-04-19T23:59:59.999+07:00");

        // Assert

        OrderStatResponseDto[] outputs = underTest.getOrderStat(fromDate, toDate, timeZone, userId, interval)
                .toArray(OrderStatResponseDto[]::new);
        OrderStatResponseDto[] expects = expectData.getExpect();

        for (int i = 0; i < outputs.length; ++i) {
            OrderStatResponseDto output = outputs[i];
            OrderStatResponseDto expect = expects[i];
            assertThat(output.getInterval()).isEqualTo(expect.getInterval());
            assertThat(output.getQuantity()).isEqualTo(output.getQuantity());
        }

        // Clean data

        mongoTemplate.getCollection("orders").drop();
    }
}

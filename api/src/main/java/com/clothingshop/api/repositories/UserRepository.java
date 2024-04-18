package com.clothingshop.api.repositories;

import com.clothingshop.api.domain.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.username = :username, u.fullName = :fullName, u.email = :email, u.phone = :phone, u.address = :address, u.img = :img where u.id = :id")
    void updateUser(@Param(value = "id") long id,
            @Param(value = "username") String username,
            @Param(value = "fullName") String fullName,
            @Param(value = "email") String email,
            @Param(value = "phone") String phone,
            @Param(value = "address") String address,
            @Param(value = "img") String img);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

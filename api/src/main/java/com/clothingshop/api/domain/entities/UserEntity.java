package com.clothingshop.api.domain.entities;

import com.clothingshop.api.domain.enums.Role;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String fullName;

    @Column(nullable = false)
    String password;

    @Transient
    Role[] roles;

    @Basic
    @Type(IntArrayType.class)
    @Column(name = "roles", columnDefinition = "smallint[]")
    private short[] roleEnumValues;

    @PostLoad
    void fillTransient() {
        this.roles = new Role[roleEnumValues.length];

        for (int i = 0; i < roleEnumValues.length; ++i) {
            this.roles[i] = Role.values()[roleEnumValues[i]];
        }

        authorities = Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role.toString())).toList();
    }

    @PrePersist
    void fillPersistent() {
        this.roleEnumValues = new short[roles.length];
        for (int i = 0; i < roleEnumValues.length; ++i) {
            this.roleEnumValues[i] = (short) roles[i].ordinal();
        }
    }

    String img;

    String phone;

    String address;

    @CreatedDate
    Date createdAt;

    @LastModifiedDate
    Date updatedAt;

    @Transient
    Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.deliverytech.delivery.entity.User;

public interface IUserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(String login);
}
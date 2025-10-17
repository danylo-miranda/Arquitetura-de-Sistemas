package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}

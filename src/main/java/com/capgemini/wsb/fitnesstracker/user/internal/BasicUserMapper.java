package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.BasicUserDto;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Component;

@Component
class BasicUserMapper {

    BasicUserDto toDto(User user) {
        return new BasicUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName());
    }
}

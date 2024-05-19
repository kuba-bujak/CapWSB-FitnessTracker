package com.capgemini.wsb.fitnesstracker.user.api;

import jakarta.annotation.Nullable;

public record BasicUserDto(@Nullable Long Id, String firstName, String lastName) {
}

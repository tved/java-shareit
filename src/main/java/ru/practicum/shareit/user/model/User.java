package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class User {
    Long id;
    String email;
    String name;
}

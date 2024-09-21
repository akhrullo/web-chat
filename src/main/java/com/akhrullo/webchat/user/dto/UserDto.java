package com.akhrullo.webchat.user.dto;

import com.akhrullo.webchat.common.UserRole;
import com.akhrullo.webchat.common.UserState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private UserState state;

    private UserRole role;
}

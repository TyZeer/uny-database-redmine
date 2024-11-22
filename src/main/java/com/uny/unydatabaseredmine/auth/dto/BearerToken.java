package com.proj.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BearerToken {
    private String accessToken;
    private String tokenType;
}
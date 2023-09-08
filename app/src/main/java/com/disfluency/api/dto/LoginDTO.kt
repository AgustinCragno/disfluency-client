package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginDTO(
    @JsonProperty("refreshToken") val refreshToken: String,
    @JsonProperty("accessToken") val accessToken: String,
    @JsonProperty("userRoleDTO") val user: RoleDTO
)
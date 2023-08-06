package com.disfluency.api.dto

import com.disfluency.model.UserRole
import com.fasterxml.jackson.annotation.JsonProperty

data class LoginDTO(
    @JsonProperty("refreshToken") val refreshToken: String,
    @JsonProperty("accessToken") val accessToken: String,
    @JsonProperty("user") val user: RoleDTO
)
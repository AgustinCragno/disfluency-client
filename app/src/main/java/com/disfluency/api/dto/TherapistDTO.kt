package com.disfluency.api.dto

import com.disfluency.model.Therapist
import com.disfluency.model.UserRole
import com.fasterxml.jackson.annotation.JsonProperty

class TherapistDTO(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("profilePictureUrl") val profilePictureUrl: Int
) : RoleDTO {

    override fun toRole(): UserRole {
        return Therapist(id)
    }
}
package com.disfluency.api.dto

import com.disfluency.model.user.Therapist
import com.disfluency.model.user.UserRole
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
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
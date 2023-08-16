package com.disfluency.api.dto

data class NewTherapistUserDTO(
    val account: String,
    val password: String,
    val user: NewTherapistDTO
)

data class NewTherapistDTO(
    val name: String,
    val lastName: String,
    val profilePictureUrl: Int
)
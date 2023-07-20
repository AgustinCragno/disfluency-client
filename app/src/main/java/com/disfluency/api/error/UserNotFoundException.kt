package com.disfluency.api.error

class UserNotFoundException(account: String) : Exception("User with account '$account' could not be found")

class TherapistNotFoundException(id: String) : Exception("Therapist with id '$id' could not be found")
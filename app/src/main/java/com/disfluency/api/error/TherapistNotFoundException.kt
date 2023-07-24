package com.disfluency.api.error

class TherapistNotFoundException(id: String) : Exception("Therapist with id '$id' could not be found")
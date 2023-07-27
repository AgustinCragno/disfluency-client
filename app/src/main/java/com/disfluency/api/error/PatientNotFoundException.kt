package com.disfluency.api.error

class PatientNotFoundException(id: String) : Exception("Patient with id '$id' could not be found")
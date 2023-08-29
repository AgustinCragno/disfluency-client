package com.disfluency.api.error

class PatientConfirmationException(id: String) : Exception("An error occurred when confirming patient '$id'")
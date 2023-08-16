package com.disfluency.api.error

class TherapistCreationException(account: String) : Exception("An error occurred when creating therapist for'${account}'")
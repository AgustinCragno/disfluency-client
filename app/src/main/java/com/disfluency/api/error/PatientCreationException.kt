package com.disfluency.api.error

import com.disfluency.model.user.Patient

class PatientCreationException(patient: Patient) : Exception("An error occurred when creating patient '${patient.fullName()}'")
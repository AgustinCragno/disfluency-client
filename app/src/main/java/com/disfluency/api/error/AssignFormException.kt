package com.disfluency.api.error

class AssignFormException(formIds: List<String>, patientIds: List<String>) : Exception("An error occurred assigning forms '${formIds}' to patients '${patientIds}'")
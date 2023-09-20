package com.disfluency.api.error

class FormAssignmentNotFoundException(id: String) : Exception("Assignment with id '$id' could not be found")
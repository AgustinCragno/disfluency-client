package com.disfluency.api.error

class FormEntryCreationException(assignmentId: String) : Exception("An error occurred when creating form entry for assignment '$assignmentId'")
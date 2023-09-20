package com.disfluency.api.error

class AnalysisNotFoundException(id: String) : Exception("Analysis with id '$id' could not be found")
package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PracticeDTO(@JsonProperty("recordingUrl") val recordingUrl: String)
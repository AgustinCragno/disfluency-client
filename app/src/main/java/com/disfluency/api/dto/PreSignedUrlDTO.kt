package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PreSignedUrlDTO(@JsonProperty("preSignedUrl") val recordingUrl: String)
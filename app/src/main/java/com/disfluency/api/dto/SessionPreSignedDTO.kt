package com.disfluency.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SessionPreSignedDTO(@JsonProperty("preSignedUrl") val recordingUrl: String)
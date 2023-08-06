package com.disfluency.api.error

class ExpiredTokenException(refreshToken: String) : Exception("No user with refresh token: '$refreshToken' could be found")
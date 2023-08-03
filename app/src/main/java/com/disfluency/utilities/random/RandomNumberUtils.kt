package com.disfluency.utilities.random

import kotlin.random.Random

/**
 * @return a random value of either 1 or -1
 */
fun randomSign(): Int = (Random.nextInt(0, 2) * 2) - 1
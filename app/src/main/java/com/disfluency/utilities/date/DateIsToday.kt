package com.disfluency.utilities.date

import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDate.equalsDay(otherDate: LocalDate): Boolean =
    this.year == otherDate.year
    && this.monthValue == otherDate.monthValue
    && this.dayOfMonth == otherDate.dayOfMonth

fun LocalDate.isToday(): Boolean = this.equalsDay(LocalDate.now())


fun LocalDateTime.equalsDay(otherDate: LocalDateTime): Boolean =
    this.year == otherDate.year
            && this.monthValue == otherDate.monthValue
            && this.dayOfMonth == otherDate.dayOfMonth

fun LocalDateTime.isToday(): Boolean = this.equalsDay(LocalDateTime.now())
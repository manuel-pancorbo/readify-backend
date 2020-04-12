package com.readify.shared.domain.clock

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

private val MADRID_TIMEZONE = ZoneId.of("Europe/Madrid")

class Clock {
    fun now(): ZonedDateTime = ZonedDateTime.now(MADRID_TIMEZONE)
    fun from(instant: Instant): ZonedDateTime = instant.atZone(MADRID_TIMEZONE)
    fun toUtc(dateTime: ZonedDateTime): ZonedDateTime = dateTime.toInstant().atZone(ZoneOffset.UTC)
    fun fromUtc(dateTime: ZonedDateTime) : ZonedDateTime = dateTime.toInstant().atZone(MADRID_TIMEZONE)
}
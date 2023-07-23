package com.disfluency.api.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.DayOfWeek

object DayOfWeekSerializer : JsonSerializer<List<DayOfWeek>>() {

    override fun serialize(
        value: List<DayOfWeek>,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        with(gen) {
            writeArray(value.map { d -> d.toString() }.toTypedArray(), 0, value.size)
        }
    }
}
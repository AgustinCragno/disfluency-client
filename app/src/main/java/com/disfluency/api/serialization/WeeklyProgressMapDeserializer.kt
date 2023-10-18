package com.disfluency.api.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.patrykandpatrick.vico.core.extension.getFieldValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Comparator

object WeeklyProgressMapDeserializer : JsonDeserializer<Map<LocalDate, Int>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Map<LocalDate, Int> {
        val node = p.readValueAsTree<JsonNode>()

        val map = mutableMapOf<LocalDate, Int>()

        node.fields().forEach {
            val date = LocalDate.parse(it.key)
            val value = it.value.intValue()

            map[date] = value
        }

        return map.toSortedMap(Comparator.naturalOrder())
    }
}
package com.disfluency.api.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import java.time.DayOfWeek

object DayOfWeekDeserializer : JsonDeserializer<List<DayOfWeek>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): List<DayOfWeek> {
        val node = p.readValueAsTree<JsonNode>()
        return node.map { d -> DayOfWeek.valueOf(d.textValue()) }
    }
}
package com.katerina.todoapp.data.api.serializers

import com.katerina.todoapp.domain.utils.TaskImportance
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TaskImportanceSerializer : KSerializer<TaskImportance> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("importance", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TaskImportance) {
        encoder.encodeString(TaskImportance.toServerImportance(value))
    }

    override fun deserialize(decoder: Decoder): TaskImportance {
        val value = decoder.decodeString()
        return TaskImportance.fromServerImportance(value)
    }
}
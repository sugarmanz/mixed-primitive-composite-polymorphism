import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


/** Sealed data structure that represents license information */
@Serializable(License.Serializer::class)
sealed class License {

    /** License defined as a simple string */
    @Serializable(Primitive.Serializer::class)
    data class Primitive(val license: String) : License() {

        /** Custom serializer to decode and encode [Primitive] as a [String] */
        object Serializer : KSerializer<Primitive> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("License.Primitive", PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): Primitive =
                decoder.decodeString().let(::Primitive)

            override fun serialize(encoder: Encoder, value: Primitive) =
                encoder.encodeString(value.license)
        }
    }

    /** License defined as an object containing the [name] and [url] */
    @Serializable
    data class Boxed(val name: String, val url: String) : License()

    /** Custom serializer to decode and encode [License]s */
    object Serializer : KSerializer<License> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("License", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): License = try {
            // I don't love this approach because it doesn't seem like it would scale well,
            // but it works for simple cases. Although, it might be creating the issue
            // with decoding a collection of [License]s.
            decoder.decodeString().let(::Primitive)
        } catch (exception: Exception) {
            decoder.decodeSerializableValue(Boxed.serializer())
        }

        override fun serialize(encoder: Encoder, value: License) = when (value) {
            is Primitive -> encoder.encodeSerializableValue(Primitive.serializer(), value)
            is Boxed -> encoder.encodeSerializableValue(Boxed.serializer(), value)
        }
    }

}
# Mixed Primitive-Composite Polymorphism

Consider the following JSON spec defining two different ways to declare licenses:

```json
{
  "license": "ISC"
}
```

```json
{
  "license": {
    "name": "ISC",
    "url": "https://opensource.org/licenses/ISC"
  }
}
```

Optionally, these licenses can be specified as an array:

```json
{
  "licenses": [
    "ISC",
    {
      "name": "ISC",
      "url": "https://opensource.org/licenses/ISC"
    }
  ]
}
```

### Attempt at a solution

Create a sealed class the contains an implementation with a custom serializer to encode and decode as a string and a custom serializer for the sealed class to attempt to delegate to the right serializer. See `License.kt`.

This approach seems works when decoding a single `License` field:

```kotlin
data class Metadata(
    val license: License,
)
```

But fails with an error when trying to decode a collection of `License`s:

```kotlin
data class Metadata(
    val licenses: List<License>,
)
```

```
Expected class kotlinx.serialization.json.JsonObject as the serialized body of License.Boxed, but had class kotlinx.serialization.json.JsonArray
kotlinx.serialization.json.internal.JsonDecodingException: Expected class kotlinx.serialization.json.JsonObject as the serialized body of License.Boxed, but had class kotlinx.serialization.json.JsonArray
	at kotlinx.serialization.json.internal.JsonExceptionsKt.JsonDecodingException(JsonExceptions.kt:24)
	at kotlinx.serialization.json.internal.AbstractJsonTreeDecoder.beginStructure(TreeJsonDecoder.kt:347)
	at License$Boxed$$serializer.deserialize(License.kt:32)
	at License$Boxed$$serializer.deserialize(License.kt:32)
	at kotlinx.serialization.json.internal.PolymorphicKt.decodeSerializableValuePolymorphic(Polymorphic.kt:59)
	at kotlinx.serialization.json.internal.AbstractJsonTreeDecoder.decodeSerializableValue(TreeJsonDecoder.kt:51)
	at License$Serializer.deserialize(License.kt:46)
	at License$Serializer.deserialize(License.kt:36)
	at kotlinx.serialization.json.internal.PolymorphicKt.decodeSerializableValuePolymorphic(Polymorphic.kt:59)
	at kotlinx.serialization.json.internal.AbstractJsonTreeDecoder.decodeSerializableValue(TreeJsonDecoder.kt:51)
	at kotlinx.serialization.internal.TaggedDecoder.decodeSerializableValue(Tagged.kt:206)
	at kotlinx.serialization.internal.TaggedDecoder$decodeSerializableElement$1.invoke(Tagged.kt:279)
	at kotlinx.serialization.internal.TaggedDecoder.tagBlock(Tagged.kt:296)
	at kotlinx.serialization.internal.TaggedDecoder.decodeSerializableElement(Tagged.kt:279)
	at kotlinx.serialization.encoding.CompositeDecoder$DefaultImpls.decodeSerializableElement$default(Decoding.kt:535)
	at kotlinx.serialization.internal.ListLikeSerializer.readElement(CollectionSerializers.kt:80)
	at kotlinx.serialization.internal.AbstractCollectionSerializer.readElement$default(CollectionSerializers.kt:51)
	at kotlinx.serialization.internal.AbstractCollectionSerializer.merge(CollectionSerializers.kt:36)
	at kotlinx.serialization.internal.AbstractCollectionSerializer.deserialize(CollectionSerializers.kt:43)
	at kotlinx.serialization.json.internal.PolymorphicKt.decodeSerializableValuePolymorphic(Polymorphic.kt:59)
	at kotlinx.serialization.json.internal.AbstractJsonTreeDecoder.decodeSerializableValue(TreeJsonDecoder.kt:51)
	at kotlinx.serialization.json.internal.TreeJsonDecoderKt.readJson(TreeJsonDecoder.kt:24)
	at kotlinx.serialization.json.Json.decodeFromJsonElement(Json.kt:119)
	at LicenseTest.collection of primitive and objects(LicenseTest.kt:122)
```

package infrastructure.serialization

import io.javalin.json.JsonMapper
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.serializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.reflect.Type

class CustomJsonMapper: JsonMapper {
    @Suppress("UNCHECKED_CAST")
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    override fun <T : Any> fromJsonStream(json: InputStream, targetType: Type): T {
        val serializer = serializer(targetType) as KSerializer<T>
        return Json.decodeFromStream(serializer, json)
    }

    @Suppress("UNCHECKED_CAST")
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    override fun <T : Any> fromJsonString(json: String, targetType: Type): T {
        val serializer = serializer(targetType) as KSerializer<T>
        return Json.decodeFromString(serializer, json)
    }

    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    override fun toJsonString(obj: Any, type: Type): String {
        val serializer = serializer(type)
        return Json.encodeToString(serializer, obj)
    }

    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    override fun toJsonStream(obj: Any, type: Type): InputStream {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        val serializer = serializer(type)
        Json.encodeToStream(serializer, obj, objectOutputStream)
        flushAndClose(objectOutputStream)
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        flushAndClose(byteArrayOutputStream)
        return byteArrayInputStream
    }

    private fun flushAndClose(stream: OutputStream) {
        stream.flush()
        stream.close()
    }
}
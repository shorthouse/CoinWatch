package dev.shorthouse.coinwatch.data.source.local.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

abstract class BasePreferencesSerializer<T>(
    private val defaultInstance: () -> T,
    private val serializer: KSerializer<T>,
) : Serializer<T> {

    override val defaultValue: T
        get() = defaultInstance()

    override suspend fun readFrom(input: InputStream): T {
        return try {
            Json.Default.decodeFromString(
                deserializer = serializer,
                string = input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            Timber.Forest.e("Error serializing preferences with ${serializer.descriptor}", exception)
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        output.write(
            Json.Default.encodeToString(
                serializer = serializer,
                value = t
            ).encodeToByteArray()
        )
    }
}

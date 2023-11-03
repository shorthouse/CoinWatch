package dev.shorthouse.coinwatch.data.source.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

class ImmutableListTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromImmutableList(immutableBigDecimals: ImmutableList<BigDecimal>): String {
        return gson.toJson(immutableBigDecimals)
    }

    @TypeConverter
    fun toImmutableList(immutableBigDecimalsJson: String): ImmutableList<BigDecimal> {
        val type: Type = object : TypeToken<ImmutableList<BigDecimal>>() {}.type
        return gson.fromJson<ImmutableList<BigDecimal>>(immutableBigDecimalsJson, type)
            .toPersistentList()
    }
}

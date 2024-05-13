package dev.shorthouse.coinwatch.data.source.local.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.shorthouse.coinwatch.model.Price

class PriceTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromPrice(price: Price): String {
        return gson.toJson(price)
    }

    @TypeConverter
    fun toPrice(priceJson: String): Price {
        return gson.fromJson(priceJson, Price::class.java)
    }
}

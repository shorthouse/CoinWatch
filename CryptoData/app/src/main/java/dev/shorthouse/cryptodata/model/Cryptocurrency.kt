package dev.shorthouse.cryptodata.model

import com.google.gson.annotations.SerializedName

data class Cryptocurrency(
    val id: String,

    val name: String,

    val symbol: String,

    val rank: Int,

    @SerializedName("is_new")
    val isNew: Boolean,

    @SerializedName("is_active")
    val isActive: Boolean,

    val type: String,
)

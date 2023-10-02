package dev.shorthouse.coinwatch.common

interface Mapper<F, T> {
    fun mapApiModelToModel(from: F): T
}

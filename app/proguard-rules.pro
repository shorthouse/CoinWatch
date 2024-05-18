-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# https://github.com/square/retrofit/issues/3751#issuecomment-1192043644
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Keep Retrofit API data models
-keepclasseswithmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of GSON TypeToken and its subclasses
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken

# Keep user preferences enums
-keep public enum dev.shorthouse.coinwatch.data.source.local.preferences.**{
    *;
}

# Resolve strange R8 / Proguard issue of small ImmutableList
# (Less than 30 items uses SmallPersistentVector)
# converting from ImmutableList<BigDecimal> to ImmutableList<Double>.
-keep,allowobfuscation,allowshrinking class kotlinx.collections.immutable.ImmutableList

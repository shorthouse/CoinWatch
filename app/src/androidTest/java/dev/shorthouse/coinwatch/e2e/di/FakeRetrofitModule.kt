package dev.shorthouse.coinwatch.e2e.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.shorthouse.coinwatch.data.source.remote.CoinApi
import dev.shorthouse.coinwatch.di.RetrofitModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RetrofitModule::class]
)
object FakeRetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        error("Real OkHttpClient must not be used in tests, replace at the data-source layer")

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        error("Real Retrofit must not be used in tests, replace at the data-source layer")

    @Provides
    @Singleton
    fun provideCoinApi(): CoinApi =
        error("Real CoinApi must not be used in tests, extend FakeCoinNetworkDataSource instead")
}

package dev.shorthouse.coinwatch.e2e.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.NetworkDataSourceModule
import dev.shorthouse.coinwatch.e2e.fake.FakeCoinNetworkDataSource
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkDataSourceModule::class]
)
object FakeNetworkDataSourceModule {

    @Provides
    @Singleton
    fun provideFakeCoinNetworkDataSource(): FakeCoinNetworkDataSource {
        return FakeCoinNetworkDataSource()
    }

    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(
        fake: FakeCoinNetworkDataSource,
    ): CoinNetworkDataSource = fake
}

package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class IsCoinFavouriteUseCaseTest {

    // Class under test
    private lateinit var isCoinFavouriteUseCase: IsCoinFavouriteUseCase

    @MockK
    private lateinit var favouriteCoinRepository: FavouriteCoinRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        isCoinFavouriteUseCase = IsCoinFavouriteUseCase(
            favouriteCoinRepository = favouriteCoinRepository
        )
    }

    @Test
    fun `When use case invoked should return if coin is favourite`() {
        // Arrange
        val coinId = "Qwsogvtv82FCd"

        every {
            favouriteCoinRepository.isCoinFavourite(coinId = coinId)
        } returns flowOf(Result.Success(true))

        // Act
        isCoinFavouriteUseCase(coinId = coinId)

        // Assert
        verifySequence {
            favouriteCoinRepository.isCoinFavourite(coinId = coinId)
        }
    }
}

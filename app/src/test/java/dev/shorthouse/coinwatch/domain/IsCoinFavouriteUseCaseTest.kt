package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
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
    private lateinit var favouriteCoinIdRepository: FavouriteCoinIdRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        isCoinFavouriteUseCase = IsCoinFavouriteUseCase(
            favouriteCoinIdRepository = favouriteCoinIdRepository
        )
    }

    @Test
    fun `When use case invoked should return if coin is favourite`() {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        every {
            favouriteCoinIdRepository.isCoinFavourite(favouriteCoinId = favouriteCoinId)
        } returns flowOf(Result.Success(true))

        // Act
        isCoinFavouriteUseCase(favouriteCoinId = favouriteCoinId)

        // Assert
        verifySequence {
            favouriteCoinIdRepository.isCoinFavourite(favouriteCoinId = favouriteCoinId)
        }
    }
}

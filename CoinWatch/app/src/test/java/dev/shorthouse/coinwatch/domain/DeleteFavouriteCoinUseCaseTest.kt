package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteFavouriteCoinUseCaseTest {

    // Class under test
    private lateinit var deleteFavouriteCoinUseCase: DeleteFavouriteCoinUseCase

    @MockK
    private lateinit var favouriteCoinRepository: FavouriteCoinRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        deleteFavouriteCoinUseCase = DeleteFavouriteCoinUseCase(
            favouriteCoinRepository = favouriteCoinRepository
        )
    }

    @Test
    fun `when deleteFavouriteCoinUseCase invoked then deleteFavouriteCoin is called`() = runTest {
        // Arrange
        val favouriteCoin = mockkClass(FavouriteCoin::class)

        coEvery {
            favouriteCoinRepository.deleteFavouriteCoin(favouriteCoin = favouriteCoin)
        } just Runs

        // Act
        deleteFavouriteCoinUseCase(favouriteCoin = favouriteCoin)

        // Assert
        coVerifySequence {
            favouriteCoinRepository.deleteFavouriteCoin(favouriteCoin = favouriteCoin)
        }
    }
}

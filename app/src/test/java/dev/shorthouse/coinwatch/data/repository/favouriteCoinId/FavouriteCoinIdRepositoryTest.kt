package dev.shorthouse.coinwatch.data.repository.favouriteCoinId

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.database.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavouriteCoinIdRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var favouriteCoinIdRepository: FavouriteCoinIdRepository

    @MockK
    private lateinit var coinLocalDataSource: CoinLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        favouriteCoinIdRepository = FavouriteCoinIdRepositoryImpl(
            coinLocalDataSource = coinLocalDataSource,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When get favourite coin Ids with valid data should return success`() = runTest {
        // Arrange
        every { coinLocalDataSource.getFavouriteCoinIds() } returns flowOf(
            listOf(
                FavouriteCoinId(id = "Qwsogvtv82FCd"),
                FavouriteCoinId(id = "razxDUgYGNAdQ")
            )
        )

        val expectedResult = Result.Success(
            listOf(
                FavouriteCoinId(id = "Qwsogvtv82FCd"),
                FavouriteCoinId(id = "razxDUgYGNAdQ")
            )
        )

        // Act
        val result = favouriteCoinIdRepository.getFavouriteCoinIds().first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When is coin favourite is called on favourited coin should return true`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        every {
            coinLocalDataSource.isCoinFavourite(favouriteCoinId)
        } returns flowOf(true)

        val expectedResult = Result.Success(true)

        // Act
        val result = favouriteCoinIdRepository.isCoinFavourite(favouriteCoinId).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When is coin favourite is called on not favourited coin should return false`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        every {
            coinLocalDataSource.isCoinFavourite(favouriteCoinId)
        } returns flowOf(false)

        val expectedResult = Result.Success(false)

        // Act
        val result = favouriteCoinIdRepository.isCoinFavourite(favouriteCoinId).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When toggling coin favourite should call toggle coin favourite`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        coEvery {
            coinLocalDataSource.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)
        } just Runs

        // Act
        favouriteCoinIdRepository.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)

        // Assert
        coVerifySequence {
            favouriteCoinIdRepository.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)
        }
    }
}

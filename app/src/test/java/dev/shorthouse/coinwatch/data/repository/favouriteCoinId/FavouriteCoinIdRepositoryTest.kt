package dev.shorthouse.coinwatch.data.repository.favouriteCoinId

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.database.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

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
    fun `When getting favourite coin ids throws should return error`() = runTest {
        // Arrange
        val errorMessage = "Unable to fetch favourite coin ids"

        every { coinLocalDataSource.getFavouriteCoinIds() } returns flow {
            throw IOException("DB read failed")
        }

        val expectedResult = Result.Error<List<FavouriteCoinId>>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinIdRepository.getFavouriteCoinIds().first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When is coin favourite throws should return error`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")
        val errorMessage = "Unable to fetch if coin is favourite"

        every {
            coinLocalDataSource.isCoinFavourite(favouriteCoinId)
        } returns flow {
            throw IOException("DB read failed")
        }

        val expectedResult = Result.Error<Boolean>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinIdRepository.isCoinFavourite(favouriteCoinId).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When toggling coin favourite should delegate to data source`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        coEvery {
            coinLocalDataSource.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)
        } returns true

        // Act
        favouriteCoinIdRepository.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)

        // Assert
        coVerifySequence {
            coinLocalDataSource.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)
        }
    }

    @Test
    fun `When toggling coin favourite returns true should return true`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        coEvery {
            coinLocalDataSource.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)
        } returns true

        // Act
        val result = favouriteCoinIdRepository.toggleIsCoinFavourite(
            favouriteCoinId = favouriteCoinId
        )

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `When toggling coin favourite returns false should return false`() = runTest {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")

        coEvery {
            coinLocalDataSource.toggleIsCoinFavourite(favouriteCoinId = favouriteCoinId)
        } returns false

        // Act
        val result = favouriteCoinIdRepository.toggleIsCoinFavourite(
            favouriteCoinId = favouriteCoinId
        )

        // Assert
        assertThat(result).isFalse()
    }
}

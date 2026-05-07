package dev.shorthouse.coinwatch.data.source.local.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import java.io.Closeable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinDatabaseMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        CoinDatabase::class.java
    )

    @Test
    fun migrate1To2_preservesFavouriteCoinIds() {
        helper.createDatabase(TEST_DB, 1).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_1)
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_2)
        }

        helper.runMigrationsAndValidate(TEST_DB, 2, validateDroppedTables = true).useApply {
            assertTableExists("FavouriteCoin")
            assertTableExists("CachedCoin")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoin",
                expectedIds = listOf(FAVOURITE_COIN_ID_1, FAVOURITE_COIN_ID_2)
            )
        }
    }

    @Test
    fun migrate2To3_renamesFavouriteCoinToFavouriteCoinId() {
        helper.createDatabase(TEST_DB, 2).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_1)
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_2)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            insertCoin(
                tableName = "CachedCoin",
                id = CACHED_COIN_ID_2,
                name = ETHEREUM_NAME,
                symbol = ETHEREUM_SYMBOL
            )
        }

        helper.runMigrationsAndValidate(TEST_DB, 3, validateDroppedTables = true).useApply {
            assertTableDoesNotExist("FavouriteCoin")
            assertTableExists("FavouriteCoinId")
            assertTableExists("CachedCoin")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoinId",
                expectedIds = listOf(FAVOURITE_COIN_ID_1, FAVOURITE_COIN_ID_2)
            )
            assertCoinRow(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            assertCoinRow(
                tableName = "CachedCoin",
                id = CACHED_COIN_ID_2,
                expectedName = ETHEREUM_NAME,
                expectedSymbol = ETHEREUM_SYMBOL
            )
        }
    }

    @Test
    fun migrate3To4_addsFavouriteCoinTable() {
        helper.createDatabase(TEST_DB, 3).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoinId", id = FAVOURITE_COIN_ID_1)
            insertFavouriteCoinId(tableName = "FavouriteCoinId", id = FAVOURITE_COIN_ID_2)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_2, name = ETHEREUM_NAME)
        }

        helper.runMigrationsAndValidate(TEST_DB, 4, validateDroppedTables = true).useApply {
            assertTableExists("FavouriteCoinId")
            assertTableExists("CachedCoin")
            assertTableExists("FavouriteCoin")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoinId",
                expectedIds = listOf(FAVOURITE_COIN_ID_1, FAVOURITE_COIN_ID_2)
            )
            assertCoinRow(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            assertCoinRow(
                tableName = "CachedCoin",
                id = CACHED_COIN_ID_2,
                expectedName = ETHEREUM_NAME
            )
            assertRowCount(tableName = "FavouriteCoin", expectedCount = 0)
        }
    }

    @Test
    fun migrate4To5_renamesCachedCoinToCoin() {
        helper.createDatabase(TEST_DB, 4).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoinId", id = FAVOURITE_COIN_ID_1)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_2, name = ETHEREUM_NAME)
            insertCoin(tableName = "FavouriteCoin", id = FAVOURITE_FULL_COIN_ID_1)
            insertCoin(
                tableName = "FavouriteCoin",
                id = FAVOURITE_FULL_COIN_ID_2,
                name = SOLANA_NAME
            )
        }

        helper.runMigrationsAndValidate(TEST_DB, 5, validateDroppedTables = true).useApply {
            assertTableDoesNotExist("CachedCoin")
            assertTableExists("Coin")
            assertTableExists("FavouriteCoin")
            assertTableExists("FavouriteCoinId")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoinId",
                expectedIds = listOf(FAVOURITE_COIN_ID_1)
            )
            assertCoinRow(tableName = "Coin", id = CACHED_COIN_ID_1)
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_2,
                expectedName = ETHEREUM_NAME
            )
            assertCoinRow(tableName = "FavouriteCoin", id = FAVOURITE_FULL_COIN_ID_1)
            assertCoinRow(
                tableName = "FavouriteCoin",
                id = FAVOURITE_FULL_COIN_ID_2,
                expectedName = SOLANA_NAME
            )
        }
    }

    @Test
    fun migrate5To6_dropsOnlyCoinPrices24h() {
        helper.createDatabase(TEST_DB, 5).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoinId", id = FAVOURITE_COIN_ID_1)
            insertCoin(tableName = "Coin", id = CACHED_COIN_ID_1)
            insertCoin(tableName = "Coin", id = CACHED_COIN_ID_2, name = ETHEREUM_NAME)
            insertCoin(tableName = "FavouriteCoin", id = FAVOURITE_FULL_COIN_ID_1)
            insertCoin(
                tableName = "FavouriteCoin",
                id = FAVOURITE_FULL_COIN_ID_2,
                name = SOLANA_NAME,
                prices24h = ALT_PRICES_24H
            )
        }

        helper.runMigrationsAndValidate(TEST_DB, 6, validateDroppedTables = true).useApply {
            assertTableExists("Coin")
            assertTableExists("FavouriteCoin")
            assertTableExists("FavouriteCoinId")
            assertColumnDoesNotExist(tableName = "Coin", columnName = "prices24h")
            assertColumnExists(tableName = "FavouriteCoin", columnName = "prices24h")
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_1,
                expectedPrices24h = null
            )
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_2,
                expectedName = ETHEREUM_NAME,
                expectedPrices24h = null
            )
            assertCoinRow(tableName = "FavouriteCoin", id = FAVOURITE_FULL_COIN_ID_1)
            assertCoinRow(
                tableName = "FavouriteCoin",
                id = FAVOURITE_FULL_COIN_ID_2,
                expectedName = SOLANA_NAME,
                expectedPrices24h = ALT_PRICES_24H
            )
        }
    }

    @Test
    fun migrateAllFrom1To6_matchesLatestSchema() {
        helper.createDatabase(TEST_DB, 1).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_1)
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_2)
        }

        helper.runMigrationsAndValidate(TEST_DB, 6, validateDroppedTables = true).useApply {
            assertTableExists("Coin")
            assertTableExists("FavouriteCoin")
            assertTableExists("FavouriteCoinId")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoinId",
                expectedIds = listOf(FAVOURITE_COIN_ID_1, FAVOURITE_COIN_ID_2)
            )
            assertRowCount(tableName = "Coin", expectedCount = 0)
            assertRowCount(tableName = "FavouriteCoin", expectedCount = 0)
        }
    }

    @Test
    fun migrateAllFrom2To6_preservesCoinDataThroughChain() {
        helper.createDatabase(TEST_DB, 2).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_1)
            insertFavouriteCoinId(tableName = "FavouriteCoin", id = FAVOURITE_COIN_ID_2)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_2, name = ETHEREUM_NAME)
        }

        helper.runMigrationsAndValidate(TEST_DB, 6, validateDroppedTables = true).useApply {
            assertTableExists("Coin")
            assertTableExists("FavouriteCoin")
            assertTableExists("FavouriteCoinId")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoinId",
                expectedIds = listOf(FAVOURITE_COIN_ID_1, FAVOURITE_COIN_ID_2)
            )
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_1,
                expectedPrices24h = null
            )
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_2,
                expectedName = ETHEREUM_NAME,
                expectedPrices24h = null
            )
            assertRowCount(tableName = "FavouriteCoin", expectedCount = 0)
        }
    }

    @Test
    fun migrate3To6_skipsIntermediateVersions() {
        helper.createDatabase(TEST_DB, 3).useApply {
            insertFavouriteCoinId(tableName = "FavouriteCoinId", id = FAVOURITE_COIN_ID_1)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_1)
            insertCoin(tableName = "CachedCoin", id = CACHED_COIN_ID_2, name = ETHEREUM_NAME)
        }

        helper.runMigrationsAndValidate(TEST_DB, 6, validateDroppedTables = true).useApply {
            assertTableExists("Coin")
            assertTableExists("FavouriteCoin")
            assertTableExists("FavouriteCoinId")
            assertFavouriteCoinIds(
                tableName = "FavouriteCoinId",
                expectedIds = listOf(FAVOURITE_COIN_ID_1)
            )
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_1,
                expectedPrices24h = null
            )
            assertCoinRow(
                tableName = "Coin",
                id = CACHED_COIN_ID_2,
                expectedName = ETHEREUM_NAME,
                expectedPrices24h = null
            )
            assertRowCount(tableName = "FavouriteCoin", expectedCount = 0)
        }
    }

    private fun SupportSQLiteDatabase.insertFavouriteCoinId(
        tableName: String,
        id: String
    ) {
        execSQL(
            "INSERT INTO $tableName (id) VALUES (?)",
            arrayOf(id)
        )
    }

    private fun SupportSQLiteDatabase.insertCoin(
        tableName: String,
        id: String,
        name: String = CACHED_COIN_NAME,
        symbol: String = CACHED_COIN_SYMBOL,
        imageUrl: String = CACHED_COIN_IMAGE_URL,
        currentPrice: String = CURRENT_PRICE,
        priceChangePercentage24h: String = PRICE_CHANGE_PERCENTAGE_24H,
        prices24h: String = PRICES_24H
    ) {
        execSQL(
            """
                INSERT INTO $tableName (
                    id,
                    name,
                    symbol,
                    imageUrl,
                    currentPrice,
                    priceChangePercentage24h,
                    prices24h
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            arrayOf(
                id,
                name,
                symbol,
                imageUrl,
                currentPrice,
                priceChangePercentage24h,
                prices24h
            )
        )
    }

    private fun SupportSQLiteDatabase.assertTableExists(tableName: String) {
        assertThat(hasTable(tableName)).isTrue()
    }

    private fun SupportSQLiteDatabase.assertTableDoesNotExist(tableName: String) {
        assertThat(hasTable(tableName)).isFalse()
    }

    private fun SupportSQLiteDatabase.assertColumnExists(
        tableName: String,
        columnName: String
    ) {
        assertThat(hasColumn(tableName = tableName, columnName = columnName)).isTrue()
    }

    private fun SupportSQLiteDatabase.assertColumnDoesNotExist(
        tableName: String,
        columnName: String
    ) {
        assertWithMessage("Cannot check for missing column $columnName: table $tableName does not exist")
            .that(hasTable(tableName)).isTrue()
        assertThat(hasColumn(tableName = tableName, columnName = columnName)).isFalse()
    }

    private fun SupportSQLiteDatabase.assertFavouriteCoinIds(
        tableName: String,
        expectedIds: List<String>
    ) {
        query("SELECT id FROM $tableName ORDER BY id").use { cursor ->
            val actual = mutableListOf<String>()
            while (cursor.moveToNext()) {
                actual += cursor.getString(0)
            }
            assertThat(actual).containsExactlyElementsIn(expectedIds.sorted()).inOrder()
        }
    }

    private fun SupportSQLiteDatabase.assertCoinRow(
        tableName: String,
        id: String,
        expectedName: String = CACHED_COIN_NAME,
        expectedSymbol: String = CACHED_COIN_SYMBOL,
        expectedImageUrl: String = CACHED_COIN_IMAGE_URL,
        expectedCurrentPrice: String = CURRENT_PRICE,
        expectedPriceChangePercentage24h: String = PRICE_CHANGE_PERCENTAGE_24H,
        expectedPrices24h: String? = PRICES_24H
    ) {
        val columns = mutableListOf(
            "name" to expectedName,
            "symbol" to expectedSymbol,
            "imageUrl" to expectedImageUrl,
            "currentPrice" to expectedCurrentPrice,
            "priceChangePercentage24h" to expectedPriceChangePercentage24h
        )
        if (expectedPrices24h != null) {
            columns += "prices24h" to expectedPrices24h
        }

        val columnList = columns.joinToString { it.first }
        query("SELECT $columnList FROM $tableName WHERE id = ?", arrayOf(id)).use { cursor ->
            assertThat(cursor.moveToFirst()).isTrue()
            columns.forEachIndexed { index, (column, expected) ->
                assertWithMessage("$tableName.$column for id=$id")
                    .that(cursor.getString(index))
                    .isEqualTo(expected)
            }
            assertThat(cursor.moveToNext()).isFalse()
        }
    }

    private fun SupportSQLiteDatabase.assertRowCount(
        tableName: String,
        expectedCount: Int
    ) {
        query("SELECT COUNT(*) FROM $tableName").use { cursor ->
            assertThat(cursor.moveToFirst()).isTrue()
            assertThat(cursor.getInt(0)).isEqualTo(expectedCount)
        }
    }

    private fun SupportSQLiteDatabase.hasTable(tableName: String): Boolean {
        query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?",
            arrayOf(tableName)
        ).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    private fun SupportSQLiteDatabase.hasColumn(
        tableName: String,
        columnName: String
    ): Boolean {
        query("PRAGMA table_info($tableName)").use { cursor ->
            val nameColumnIndex = cursor.getColumnIndexOrThrow("name")
            while (cursor.moveToNext()) {
                if (cursor.getString(nameColumnIndex) == columnName) {
                    return true
                }
            }
            return false
        }
    }

    private inline fun <T : Closeable> T.useApply(block: T.() -> Unit) {
        use { it.block() }
    }

    private companion object {
        const val TEST_DB = "coin-database-migration-test"
        const val FAVOURITE_COIN_ID_1 = "favourite-coin-id-1"
        const val FAVOURITE_COIN_ID_2 = "favourite-coin-id-2"
        const val CACHED_COIN_ID_1 = "cached-coin-id-1"
        const val CACHED_COIN_ID_2 = "cached-coin-id-2"
        const val FAVOURITE_FULL_COIN_ID_1 = "favourite-full-coin-id-1"
        const val FAVOURITE_FULL_COIN_ID_2 = "favourite-full-coin-id-2"
        const val CACHED_COIN_NAME = "Bitcoin"
        const val CACHED_COIN_SYMBOL = "BTC"
        const val CACHED_COIN_IMAGE_URL = "https://example.com/bitcoin.svg"
        const val ETHEREUM_NAME = "Ethereum"
        const val ETHEREUM_SYMBOL = "ETH"
        const val SOLANA_NAME = "Solana"
        const val CURRENT_PRICE = """{"price":"29490.954785191607","currency":"USD"}"""
        const val PRICE_CHANGE_PERCENTAGE_24H = """{"percentage":"-0.96"}"""
        const val PRICES_24H = "[1.23,4.56]"
        const val ALT_PRICES_24H = "[7.89,10.11]"
    }
}

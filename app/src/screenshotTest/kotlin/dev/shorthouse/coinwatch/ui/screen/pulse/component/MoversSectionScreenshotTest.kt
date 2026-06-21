package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf

@PreviewTest
@Preview
@Composable
fun MoversSectionScreenshotTest() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            PulseSection(title = stringResource(R.string.pulse_movers)) {
                MoversSection(
                    movers = screenshotMovers(),
                    onCoinClick = {}
                )
            }
        }
    }
}

private fun screenshotMovers() = Movers(
    topGainer = MoverCoin(
        id = "Qwsogvtv82FCd",
        name = "Bitcoin",
        symbol = "BTC",
        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
        currentPrice = Price("89086.46", Currency.USD),
        priceChangePercentage24h = Percentage("14.27"),
        sparkline = persistentListOf(
            BigDecimal("87718"),
            BigDecimal("88217"),
            BigDecimal("88518"),
            BigDecimal("88899"),
            BigDecimal("89086")
        )
    ),
    topLoser = MoverCoin(
        id = "a91GCGd_U96cF",
        name = "Dogecoin",
        symbol = "DOGE",
        imageUrl = "https://cdn.coinranking.com/H1arVnjQ_/doge.svg",
        currentPrice = Price("0.11923", Currency.USD),
        priceChangePercentage24h = Percentage("-11.62"),
        sparkline = persistentListOf(
            BigDecimal("0.135"),
            BigDecimal("0.131"),
            BigDecimal("0.126"),
            BigDecimal("0.122"),
            BigDecimal("0.119")
        )
    ),
    mostMovement = persistentListOf(
        MoverCoin(
            id = "razxDUgYGNAdQ",
            name = "Ethereum",
            symbol = "ETH",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("3027.60", Currency.USD),
            priceChangePercentage24h = Percentage("-8.43"),
            sparkline = persistentListOf()
        ),
        MoverCoin(
            id = "zNZHO_Sjf",
            name = "Algorand",
            symbol = "ALGO",
            imageUrl = "https://cdn.coinranking.com/fztgfHckp/algorand-algo-logo.svg",
            currentPrice = Price("0.18234", Currency.USD),
            priceChangePercentage24h = Percentage("7.91"),
            sparkline = persistentListOf()
        ),
        MoverCoin(
            id = "K84RXBjBj",
            name = "Monero",
            symbol = "XMR",
            imageUrl = "https://cdn.coinranking.com/yiiHV-zUm/monero-xmr-logo.svg",
            currentPrice = Price("162.47", Currency.USD),
            priceChangePercentage24h = Percentage("-6.05"),
            sparkline = persistentListOf()
        )
    )
)

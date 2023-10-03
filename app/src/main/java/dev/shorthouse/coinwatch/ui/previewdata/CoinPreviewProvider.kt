package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf

class CoinPreviewProvider : PreviewParameterProvider<Coin> {
    override val values = sequenceOf(
        Coin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988"),
            priceChangePercentage24h = Percentage("0.76833"),
            prices24h = persistentListOf(
                BigDecimal("29245.370873051394"),
                BigDecimal("29205.501195094886"),
                BigDecimal("29210.97710800848"),
                BigDecimal("29183.90996906209"),
                BigDecimal("29191.187134377586"),
                BigDecimal("29167.309535190096"),
                BigDecimal("29223.071887272858"),
                BigDecimal("29307.753433422175"),
                BigDecimal("29267.687825355235"),
                BigDecimal("29313.499192934243"),
                BigDecimal("29296.218518715148"),
                BigDecimal("29276.651666477588"),
                BigDecimal("29343.71801186576"),
                BigDecimal("29354.73988657794"),
                BigDecimal("29614.69857297837"),
                BigDecimal("29473.762709346545"),
                BigDecimal("29460.63779255003"),
                BigDecimal("29363.672907978616"),
                BigDecimal("29325.29799021886"),
                BigDecimal("29370.611267446548"),
                BigDecimal("29390.15178296929"),
                BigDecimal("29428.222505493162"),
                BigDecimal("29475.12359313808"),
                BigDecimal("29471.20179209623")
            )
        ),
        Coin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1875.473083380222"),
            priceChangePercentage24h = Percentage("-1.11008"),
            prices24h = persistentListOf(
                BigDecimal("1879.89804628163"),
                BigDecimal("1877.1265051203513"),
                BigDecimal("1874.813847463032"),
                BigDecimal("1872.5227299255032"),
                BigDecimal("1868.6028895583647"),
                BigDecimal("1871.0393773711166"),
                BigDecimal("1870.0560363427128"),
                BigDecimal("1870.8836588622398"),
                BigDecimal("1883.596820245353"),
                BigDecimal("1872.2660234438479"),
                BigDecimal("1870.3643514336038"),
                BigDecimal("1857.714042675004"),
                BigDecimal("1859.699110729761"),
                BigDecimal("1860.670790300295"),
                BigDecimal("1857.800775098814"),
                BigDecimal("1858.8543780601171"),
                BigDecimal("1854.9767268239643"),
                BigDecimal("1850.7124615073333"),
                BigDecimal("1851.8376138000401"),
                BigDecimal("1850.1796229972815"),
                BigDecimal("1854.8824120105778"),
                BigDecimal("1853.3272421902477"),
                BigDecimal("1857.8290158859397"),
                BigDecimal("1859.4549720388395")
            )
        ),
        Coin(
            id = "tether",
            symbol = "USDT",
            name = "Tether",
            imageUrl = "https://cdn.coinranking.com/mgHqwlCLj/usdt.svg",
            currentPrice = Price("1.00"),
            priceChangePercentage24h = Percentage("0.00"),
            prices24h = persistentListOf(
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00"),
                BigDecimal("1.00")
            )
        )
    )
}

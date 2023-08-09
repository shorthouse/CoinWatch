package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import dev.shorthouse.coinwatch.ui.previewdata.CoinListPreviewData.coins
import dev.shorthouse.coinwatch.ui.screen.list.CoinListUiState
import java.math.BigDecimal

class CoinListUiStatePreviewProvider : PreviewParameterProvider<CoinListUiState> {
    override val values = sequenceOf(
        CoinListUiState.Success(
            coins = coins,
            favouriteCoins = coins,
            marketStats = MarketStats(
                marketCapChangePercentage24h = Percentage(BigDecimal("-0.23"))
            ),
            timeOfDay = TimeOfDay.Evening
        ),
        CoinListUiState.Loading,
        CoinListUiState.Error("No internet connection")
    )
}

private object CoinListPreviewData {
    val coins = listOf(
        Coin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
            currentPrice = Price(BigDecimal("29446.336548759988")),
            priceChangePercentage24h = Percentage(BigDecimal("0.76833")),
            prices24h = listOf(
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
            image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
            currentPrice = Price(BigDecimal("1875.473083380222")),
            priceChangePercentage24h = Percentage(BigDecimal("-1.11008")),
            prices24h = listOf(
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
            image = "https://assets.coingecko.com/coins/images/325/large/Tether.png?1668148663",
            currentPrice = Price(BigDecimal("1.00")),
            priceChangePercentage24h = Percentage(BigDecimal("0.00")),
            prices24h = listOf(
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
        ),
        Coin(
            id = "ripple",
            symbol = "XRP",
            name = "XRP",
            image = "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png?1605778731",
            currentPrice = Price(BigDecimal("0.7142802333064954")),
            priceChangePercentage24h = Percentage(BigDecimal("1.77031")),
            prices24h = listOf(
                BigDecimal("0.7078633715412483"),
                BigDecimal("0.703154172261876"),
                BigDecimal("0.6994823867542781"),
                BigDecimal("0.7014706603483004"),
                BigDecimal("0.69879109571246"),
                BigDecimal("0.6966649080752425"),
                BigDecimal("0.6975200860526335"),
                BigDecimal("0.7011758683759688"),
                BigDecimal("0.7021223773179766"),
                BigDecimal("0.7023799603937112"),
                BigDecimal("0.7044909385003845"),
                BigDecimal("0.7017835251269512"),
                BigDecimal("0.6995375362059472"),
                BigDecimal("0.7143777711709876"),
                BigDecimal("0.7125634338075278"),
                BigDecimal("0.727321981146483"),
                BigDecimal("0.7198675986002214"),
                BigDecimal("0.7175166290060175"),
                BigDecimal("0.7158774882632872"),
                BigDecimal("0.7091036220562065"),
                BigDecimal("0.7123303286388961"),
                BigDecimal("0.7156576118999355"),
                BigDecimal("0.7192302623965658"),
                BigDecimal("0.7186324625859829")
            )
        ),
        Coin(
            id = "binancecoin",
            symbol = "BNB",
            name = "BNB",
            image = "https://assets.coingecko.com/coins/images/825/large/bnb-icon2_2x.png?1644979850",
            currentPrice = Price(BigDecimal("242.13321783678734")),
            priceChangePercentage24h = Percentage(BigDecimal("1.84955")),
            prices24h = listOf(
                BigDecimal("238.07237986085968"),
                BigDecimal("237.59065248042927"),
                BigDecimal("237.62300826740525"),
                BigDecimal("237.2262878988098"),
                BigDecimal("237.55818626006544"),
                BigDecimal("236.80571195718406"),
                BigDecimal("237.64781722479938"),
                BigDecimal("238.2193416170009"),
                BigDecimal("238.15348489842916"),
                BigDecimal("238.20808952580057"),
                BigDecimal("237.78606577278475"),
                BigDecimal("237.09906305700125"),
                BigDecimal("238.36365737933727"),
                BigDecimal("238.5692322030582"),
                BigDecimal("239.75072819043407"),
                BigDecimal("239.16062125843843"),
                BigDecimal("239.00025751516466"),
                BigDecimal("238.94901761793733"),
                BigDecimal("238.5714730594989"),
                BigDecimal("239.27886677723362"),
                BigDecimal("239.67490966723844"),
                BigDecimal("240.13674947839255"),
                BigDecimal("240.41687032176682"),
                BigDecimal("241.82729323371586")
            )
        )
    )
}

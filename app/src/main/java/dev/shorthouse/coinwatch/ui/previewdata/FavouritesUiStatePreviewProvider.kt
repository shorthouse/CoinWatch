package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.previewdata.FavouritesPreviewData.favouriteCoins
import dev.shorthouse.coinwatch.ui.screen.favourites.FavouritesUiState
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf

class FavouritesUiStatePreviewProvider : PreviewParameterProvider<FavouritesUiState> {
    override val values = sequenceOf(
        FavouritesUiState(
            favouriteCoins = favouriteCoins
        ),
        FavouritesUiState(
            favouriteCoins = persistentListOf()
        ),
        FavouritesUiState(
            isLoading = true
        )
    )
}

private object FavouritesPreviewData {
    val favouriteCoins = persistentListOf(
        FavouriteCoin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988"),
            priceChangePercentage24h = Percentage("1.76833"),
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
        FavouriteCoin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1875.473083380222"),
            priceChangePercentage24h = Percentage("1.84"),
            prices24h = persistentListOf(
                BigDecimal("1859.4549720388395"),
                BigDecimal("1857.8290158859397"),
                BigDecimal("1853.3272421902477"),
                BigDecimal("1854.8824120105778"),
                BigDecimal("1850.1796229972815"),
                BigDecimal("1851.8376138000401"),
                BigDecimal("1850.7124615073333"),
                BigDecimal("1854.9767268239643"),
                BigDecimal("1858.8543780601171"),
                BigDecimal("1857.800775098814"),
                BigDecimal("1860.670790300295"),
                BigDecimal("1859.699110729761"),
                BigDecimal("1857.714042675004"),
                BigDecimal("1870.3643514336038"),
                BigDecimal("1872.2660234438479"),
                BigDecimal("1883.596820245353"),
                BigDecimal("1870.8836588622398"),
                BigDecimal("1870.0560363427128"),
                BigDecimal("1871.0393773711166"),
                BigDecimal("1868.6028895583647"),
                BigDecimal("1872.5227299255032"),
                BigDecimal("1874.813847463032"),
                BigDecimal("1877.1265051203513"),
                BigDecimal("1879.89804628163")
            )
        ),
        FavouriteCoin(
            id = "ripple",
            symbol = "XRP",
            name = "XRP",
            imageUrl = "https://cdn.coinranking.com/B1oPuTyfX/xrp.svg",
            currentPrice = Price("0.7142802333064954"),
            priceChangePercentage24h = Percentage("-2.37031"),
            prices24h = persistentListOf(
                BigDecimal("0.7186324625859829"),
                BigDecimal("0.7192302623965658"),
                BigDecimal("0.7156576118999355"),
                BigDecimal("0.7123303286388961"),
                BigDecimal("0.7091036220562065"),
                BigDecimal("0.7158774882632872"),
                BigDecimal("0.7175166290060175"),
                BigDecimal("0.7198675986002214"),
                BigDecimal("0.727321981146483"),
                BigDecimal("0.7125634338075278"),
                BigDecimal("0.7143777711709876"),
                BigDecimal("0.6995375362059472"),
                BigDecimal("0.7017835251269512"),
                BigDecimal("0.7044909385003845"),
                BigDecimal("0.7023799603937112"),
                BigDecimal("0.7021223773179766"),
                BigDecimal("0.7011758683759688"),
                BigDecimal("0.6975200860526335"),
                BigDecimal("0.6966649080752425"),
                BigDecimal("0.69879109571246"),
                BigDecimal("0.7014706603483004"),
                BigDecimal("0.6994823867542781"),
                BigDecimal("0.703154172261876"),
                BigDecimal("0.7078633715412483")
            )
        ),
        FavouriteCoin(
            id = "dogecoin",
            symbol = "DOGE",
            name = "Dogecoin",
            imageUrl = "https://cdn.coinranking.com/H1arXIuOZ/doge.svg",
            currentPrice = Price("0.07353603881046378"),
            priceChangePercentage24h = Percentage("3.34"),
            prices24h = persistentListOf(
                BigDecimal("0.07362674127459982"),
                BigDecimal("0.07469010791853924"),
                BigDecimal("0.07451424253764859"),
                BigDecimal("0.07472142663198798"),
                BigDecimal("0.07459446995586061"),
                BigDecimal("0.07422526738228258"),
                BigDecimal("0.07416611462618551"),
                BigDecimal("0.07466424935035157"),
                BigDecimal("0.0746843093798784"),
                BigDecimal("0.074356504292732"),
                BigDecimal("0.07410310202532591"),
                BigDecimal("0.07421693559680462"),
                BigDecimal("0.07419794500025781"),
                BigDecimal("0.07401499695685362"),
                BigDecimal("0.07405556408151426"),
                BigDecimal("0.07314547225549103"),
                BigDecimal("0.07244019654835036"),
                BigDecimal("0.07187230078163152"),
                BigDecimal("0.07111481505374677"),
                BigDecimal("0.07114822981986299"),
                BigDecimal("0.07145498779463948"),
                BigDecimal("0.07168235578136484"),
                BigDecimal("0.07246344306666684"),
                BigDecimal("0.07690600228914853")
            )
        ),
        FavouriteCoin(
            id = "polygon",
            symbol = "MATIC",
            name = "Polygon",
            imageUrl = "https://cdn.coinranking.com/M-pwilaq-/polygon-matic-logo.svg",
            currentPrice = Price("0.8245413634145558"),
            priceChangePercentage24h = Percentage("-0.89"),
            prices24h = persistentListOf(
                BigDecimal("0.8428894137317899"),
                BigDecimal("0.8045867386955229"),
                BigDecimal("0.8077164532889507"),
                BigDecimal("0.8109875455176482"),
                BigDecimal("0.8027283114580585"),
                BigDecimal("0.7979254249703243"),
                BigDecimal("0.8102190729517622"),
                BigDecimal("0.8288194437496644"),
                BigDecimal("0.8431632581705653"),
                BigDecimal("0.8515508500497216"),
                BigDecimal("0.8466906116802607"),
                BigDecimal("0.8508824009922037"),
                BigDecimal("0.8527628176400444"),
                BigDecimal("0.8475119198400725"),
                BigDecimal("0.842858148278957"),
                BigDecimal("0.8348677278081027"),
                BigDecimal("0.8285919931842739"),
                BigDecimal("0.8232350950871549"),
                BigDecimal("0.8311874758245366"),
                BigDecimal("0.8365729552746606"),
                BigDecimal("0.8399508148596185"),
                BigDecimal("0.833872406977943"),
                BigDecimal("0.8359082732576029"),
                BigDecimal("0.8240942471164237")
            )
        )
    )
}

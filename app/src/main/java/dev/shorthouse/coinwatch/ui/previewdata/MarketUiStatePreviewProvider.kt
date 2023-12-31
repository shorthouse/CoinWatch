package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.previewdata.CoinListPreviewData.coins
import dev.shorthouse.coinwatch.ui.screen.market.MarketUiState
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf

class MarketUiStatePreviewProvider : PreviewParameterProvider<MarketUiState> {
    override val values = sequenceOf(
        MarketUiState(
            coins = coins
        ),
        MarketUiState(
            coins = persistentListOf()
        ),
        MarketUiState(
            isLoading = true
        )
    )
}

private object CoinListPreviewData {
    val coins = persistentListOf(
        CachedCoin(
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
        CachedCoin(
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
        CachedCoin(
            id = "tether",
            symbol = "USDT",
            name = "Tether USD",
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
        ),
        CachedCoin(
            id = "binancecoin",
            symbol = "BNB",
            name = "BNB",
            imageUrl = "https://cdn.coinranking.com/B1N19L_dZ/bnb.svg",
            currentPrice = Price("242.13321783678734"),
            priceChangePercentage24h = Percentage("1.84955"),
            prices24h = persistentListOf(
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
        ),
        CachedCoin(
            id = "ripple",
            symbol = "XRP",
            name = "XRP",
            imageUrl = "https://cdn.coinranking.com/B1oPuTyfX/xrp.svg",
            currentPrice = Price("0.7142802333064954"),
            priceChangePercentage24h = Percentage("1.77031"),
            prices24h = persistentListOf(
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
        CachedCoin(
            id = "usdc",
            symbol = "USDC",
            name = "USDC",
            imageUrl = "https://cdn.coinranking.com/jkDf8sQbY/usdc.svg",
            currentPrice = Price("1.00"),
            priceChangePercentage24h = Percentage("0.08"),
            prices24h = persistentListOf(
                BigDecimal("0.999986200452111"),
                BigDecimal("1.0004858062672723"),
                BigDecimal("0.9999607640230107"),
                BigDecimal("1.0003094102955703"),
                BigDecimal("1.0002604280858853"),
                BigDecimal("1.0000380382807705"),
                BigDecimal("1.0001828433784854"),
                BigDecimal("1.0001051232186426"),
                BigDecimal("1.0000611238440935"),
                BigDecimal("1.0004691727055683"),
                BigDecimal("1.0000773639698013"),
                BigDecimal("1.0000980618057091"),
                BigDecimal("1.0003849073096178"),
                BigDecimal("1.0001496556628378"),
                BigDecimal("1.0003890519061438"),
                BigDecimal("1.0004125276703313"),
                BigDecimal("1.0003247930135195"),
                BigDecimal("1.000246245664962"),
                BigDecimal("1.0002540955813635"),
                BigDecimal("1.0002353892432356"),
                BigDecimal("0.999958415698578"),
                BigDecimal("1.0002285128286184"),
                BigDecimal("1.000432493909404"),
                BigDecimal("1.0000408611481826")
            )
        ),
        CachedCoin(
            id = "polkadot",
            symbol = "DOT",
            name = "Polkadot",
            imageUrl = "https://cdn.coinranking.com/V3NSSybv-/polkadot-dot.svg",
            currentPrice = Price("4.422860504529326"),
            priceChangePercentage24h = Percentage("-0.44"),
            prices24h = persistentListOf(
                BigDecimal("4.4335207642244985"),
                BigDecimal("4.419218533934902"),
                BigDecimal("4.408466485673207"),
                BigDecimal("4.4294324727491805"),
                BigDecimal("4.413899208406151"),
                BigDecimal("4.401393755728434"),
                BigDecimal("4.396723632911107"),
                BigDecimal("4.377061345398131"),
                BigDecimal("4.3560039819830845"),
                BigDecimal("4.3399040314183175"),
                BigDecimal("4.353164049533105"),
                BigDecimal("4.350395484668915"),
                BigDecimal("4.33731487488839"),
                BigDecimal("4.351328494851948"),
                BigDecimal("4.411811911359132"),
                BigDecimal("4.430526467556776"),
                BigDecimal("4.42281998566154"),
                BigDecimal("4.426950307081649"),
                BigDecimal("4.414644575485274"),
                BigDecimal("4.4112137336313175"),
                BigDecimal("4.399984935305785"),
                BigDecimal("4.413983474703376"),
                BigDecimal("4.424187893749479"),
                BigDecimal("4.421437665534955")
            )
        ),
        CachedCoin(
            id = "solana",
            symbol = "SOL",
            name = "Solana",
            imageUrl = "https://cdn.coinranking.com/yvUG4Qex5/solana.svg",
            currentPrice = Price("50.99668115384087"),
            priceChangePercentage24h = Percentage("2.45"),
            prices24h = persistentListOf(
                BigDecimal("47.33490255415595"),
                BigDecimal("45.28670329922979"),
                BigDecimal("45.26275702772549"),
                BigDecimal("45.06153458412233"),
                BigDecimal("44.32192351941288"),
                BigDecimal("44.07755319781403"),
                BigDecimal("44.97183265024955"),
                BigDecimal("45.26741033842664"),
                BigDecimal("45.40628161642721"),
                BigDecimal("45.693671563070886"),
                BigDecimal("45.86517094622826"),
                BigDecimal("46.439373225987595"),
                BigDecimal("47.49226614785766"),
                BigDecimal("47.40966164413199"),
                BigDecimal("47.25255025759465"),
                BigDecimal("47.40566558239483"),
                BigDecimal("47.922684042459764"),
                BigDecimal("47.368019494809644"),
                BigDecimal("47.534628827787465"),
                BigDecimal("49.385778537458876"),
                BigDecimal("49.48355036043628"),
                BigDecimal("51.11046141156284"),
                BigDecimal("50.97125926730215"),
                BigDecimal("49.82294581650404")
            )
        ),
        CachedCoin(
            id = "dogecoin",
            symbol = "DOGE",
            name = "Dogecoin",
            imageUrl = "https://cdn.coinranking.com/H1arXIuOZ/doge.svg",
            currentPrice = Price("0.07353603881046378"),
            priceChangePercentage24h = Percentage("-3.34"),
            prices24h = persistentListOf(
                BigDecimal("0.07690600228914853"),
                BigDecimal("0.07246344306666684"),
                BigDecimal("0.07168235578136484"),
                BigDecimal("0.07145498779463948"),
                BigDecimal("0.07114822981986299"),
                BigDecimal("0.07111481505374677"),
                BigDecimal("0.07187230078163152"),
                BigDecimal("0.07244019654835036"),
                BigDecimal("0.07314547225549103"),
                BigDecimal("0.07405556408151426"),
                BigDecimal("0.07401499695685362"),
                BigDecimal("0.07419794500025781"),
                BigDecimal("0.07421693559680462"),
                BigDecimal("0.07410310202532591"),
                BigDecimal("0.074356504292732"),
                BigDecimal("0.0746843093798784"),
                BigDecimal("0.07466424935035157"),
                BigDecimal("0.07416611462618551"),
                BigDecimal("0.07422526738228258"),
                BigDecimal("0.07459446995586061"),
                BigDecimal("0.07472142663198798"),
                BigDecimal("0.07451424253764859"),
                BigDecimal("0.07469010791853924"),
                BigDecimal("0.07362674127459982")
            )
        ),
        CachedCoin(
            id = "tron",
            symbol = "TRX",
            name = "TRON",
            imageUrl = "https://cdn.coinranking.com/behejNqQs/trx.svg",
            currentPrice = Price("0.10538265070088429"),
            priceChangePercentage24h = Percentage("1.99"),
            prices24h = persistentListOf(
                BigDecimal("0.100750718013039"),
                BigDecimal("0.09925227676540466"),
                BigDecimal("0.09861545320450688"),
                BigDecimal("0.09848344919716216"),
                BigDecimal("0.09780288558348185"),
                BigDecimal("0.09799504826355339"),
                BigDecimal("0.09849868629213138"),
                BigDecimal("0.09872742165038541"),
                BigDecimal("0.09913827570111332"),
                BigDecimal("0.09972138313082875"),
                BigDecimal("0.0998049410572683"),
                BigDecimal("0.10023406120097825"),
                BigDecimal("0.10015720859096547"),
                BigDecimal("0.10029021386292031"),
                BigDecimal("0.10037892643218804"),
                BigDecimal("0.10036508595454438"),
                BigDecimal("0.10028236907831643"),
                BigDecimal("0.09986262895696445"),
                BigDecimal("0.10004684996476221"),
                BigDecimal("0.10030067729894021"),
                BigDecimal("0.10592373153686782"),
                BigDecimal("0.10683080171541057"),
                BigDecimal("0.10609847001896261"),
                BigDecimal("0.10495869158173274")
            )
        ),
        CachedCoin(
            id = "chainlink",
            symbol = "LINK",
            name = "Chainlink",
            imageUrl = "https://cdn.coinranking.com/9NOP9tOem/chainlink.svg",
            currentPrice = Price("15.102872334646245"),
            priceChangePercentage24h = Percentage("3.28"),
            prices24h = persistentListOf(
                BigDecimal("15.525608148662764"),
                BigDecimal("14.56034906574284"),
                BigDecimal("14.693307302471034"),
                BigDecimal("14.587054997566494"),
                BigDecimal("14.353342959254485"),
                BigDecimal("14.182680691159293"),
                BigDecimal("14.473999237395846"),
                BigDecimal("14.548432886439615"),
                BigDecimal("14.653341498436413"),
                BigDecimal("14.686415540299901"),
                BigDecimal("14.5537012447306"),
                BigDecimal("14.566343503763502"),
                BigDecimal("14.480740251227074"),
                BigDecimal("14.447772378006395"),
                BigDecimal("14.401357004079431"),
                BigDecimal("14.319857153463078"),
                BigDecimal("14.349062254621924"),
                BigDecimal("14.465641707305004"),
                BigDecimal("14.59098847113666"),
                BigDecimal("14.703092860677234"),
                BigDecimal("14.859655932501466"),
                BigDecimal("14.630464722547247"),
                BigDecimal("14.942352723539658"),
                BigDecimal("14.994725659236384")
            )
        ),
        CachedCoin(
            id = "polygon",
            symbol = "MATIC",
            name = "Polygon",
            imageUrl = "https://cdn.coinranking.com/M-pwilaq-/polygon-matic-logo.svg",
            currentPrice = Price("0.8245413634145558"),
            priceChangePercentage24h = Percentage("1.44"),
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

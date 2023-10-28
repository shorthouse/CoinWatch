package dev.shorthouse.coinwatch.ui.screen.favourites.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.ui.component.PercentageChange
import dev.shorthouse.coinwatch.ui.component.PriceGraph
import dev.shorthouse.coinwatch.ui.previewdata.CoinPreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun CoinFavouriteItem(
    coin: Coin,
    onCoinClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageBuilder = remember(context) {
        ImageRequest.Builder(context = context)
            .decoderFactory(factory = SvgDecoder.Factory())
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .height(200.dp)
            .clickable { onCoinClick(coin) }
    ) {
        Column {
            Column(modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = imageBuilder
                            .data(coin.imageUrl)
                            .build(),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = coin.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = coin.symbol,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = coin.currentPrice.formattedAmount,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                PercentageChange(percentage = coin.priceChangePercentage24h)
            }

            if (coin.prices24h.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))

                PriceGraph(
                    prices = coin.prices24h,
                    priceChangePercentage = coin.priceChangePercentage24h,
                    isGraphAnimated = false,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("priceGraph ${coin.symbol}")
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.empty_chart_message_short),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun CoinFavouriteItemPreview(
    @PreviewParameter(CoinPreviewProvider::class) coin: Coin
) {
    AppTheme {
        CoinFavouriteItem(
            coin = coin,
            onCoinClick = {}
        )
    }
}

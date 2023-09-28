package dev.shorthouse.coinwatch.ui.screen.search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dev.shorthouse.coinwatch.model.SearchCoin
import dev.shorthouse.coinwatch.ui.previewdata.SearchCoinPreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun CoinSearchListItem(
    searchCoin: SearchCoin,
    onCoinClick: (SearchCoin) -> Unit,
    cardShape: Shape,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageBuilder = remember(context) {
        ImageRequest.Builder(context = context)
            .decoderFactory(factory = SvgDecoder.Factory())
    }

    Surface(
        shape = cardShape,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCoinClick(searchCoin) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            AsyncImage(
                model = imageBuilder
                    .data(searchCoin.imageUrl)
                    .build(),
                modifier = Modifier.size(32.dp),
                contentDescription = null
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = searchCoin.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = searchCoin.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
private fun CoinSearchListItemPreview(
    @PreviewParameter(SearchCoinPreviewProvider::class) searchCoin: SearchCoin
) {
    AppTheme {
        CoinSearchListItem(
            searchCoin = searchCoin,
            onCoinClick = {},
            cardShape = MaterialTheme.shapes.medium
        )
    }
}

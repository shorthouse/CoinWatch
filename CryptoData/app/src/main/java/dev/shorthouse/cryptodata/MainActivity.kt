package dev.shorthouse.cryptodata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.shorthouse.cryptodata.ui.screen.list.CoinListViewModel
import dev.shorthouse.cryptodata.ui.theme.CryptoDataTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // val coinListViewModel = CoinListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoDataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row {
        Button(
            onClick = {
                CoinListViewModel().getPost()
            },
        ) {
            Text("click me!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CryptoDataTheme {
        Greeting("Android")
    }
}

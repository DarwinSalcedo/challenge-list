package com.events.challenge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.events.challenge.presentation.ItemViewModel
import com.events.challenge.ui.theme.ChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeTheme {
                val viewModel: ItemViewModel = hiltViewModel()
                val list by viewModel.uiState.collectAsState()
                
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    Text("TITLE RANDOM LIST")
                }) { innerPadding ->
                    LazyColumn(Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        items(list) { item ->
                            ItemView(
                                title = item.title,
                                description = item.description,
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Display toast :: ${item.title}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemView(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }) {
        Text(title)
        Text(description)
    }
}

data class ItemUi(val title: String, val description: String)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChallengeTheme {
        Greeting("Android")
    }
}
package com.events.challenge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.events.challenge.presentation.ItemAction
import com.events.challenge.presentation.ItemSideEffect
import com.events.challenge.presentation.ItemUiState
import com.events.challenge.presentation.ItemViewModel
import com.events.challenge.ui.theme.ChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeTheme {
                val viewModel: ItemViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collect { effect ->
                        when (effect) {
                            is ItemSideEffect.ShowToast -> {
                                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        title = { Text("Random List Challenge") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }) { innerPadding ->
                    when (val state = uiState) {
                        is ItemUiState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Loading items...")
                            }
                        }
                        is ItemUiState.Error -> {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                            }
                        }
                        is ItemUiState.Success -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(state.items) { item ->
                                    ItemView(
                                        title = item.title,
                                        description = item.description,
                                        onClick = {
                                            viewModel.handleAction(ItemAction.ItemClicked(item))
                                        }
                                    )
                                }
                            }
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
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

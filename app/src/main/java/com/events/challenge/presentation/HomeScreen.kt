package com.events.challenge.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.events.challenge.R
import com.events.challenge.presentation.components.ErrorView
import com.events.challenge.presentation.components.LoadingView
import com.events.challenge.presentation.components.MainContentView
import com.events.challenge.ui.theme.ChallengeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
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
                title = { Text(stringResource(R.string.random_list_challenge)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }) { innerPadding ->
            when (val state = uiState) {
                is ItemUiState.Loading -> {
                    LoadingView(innerPadding)
                }

                is ItemUiState.Error -> {
                    ErrorView(
                        innerPadding = innerPadding,
                        state = state,
                        onRetry = { viewModel.handleAction(ItemAction.LoadItems) }
                    )
                }

                is ItemUiState.Success -> {
                    MainContentView(
                        innerPadding = innerPadding,
                        state = state,
                        onItemClick = { viewModel.handleAction(ItemAction.ItemClicked(it)) }
                    )
                }
            }
        }
    }
}
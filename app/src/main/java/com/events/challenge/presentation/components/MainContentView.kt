package com.events.challenge.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.events.challenge.domain.ItemModel
import com.events.challenge.presentation.ItemUiState


@Composable
fun MainContentView(
    innerPadding: PaddingValues,
    state: ItemUiState.Success,
    onItemClick: (ItemModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.items) { item ->
            ItemView(
                title = item.title,
                description = item.description,
                onClick = { onItemClick(item) }
            )
        }
    }
}
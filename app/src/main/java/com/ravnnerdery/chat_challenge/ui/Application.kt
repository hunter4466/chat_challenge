package com.ravnnerdery.chat_challenge.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.ImageLoader
import com.ravnnerdery.chat_challenge.application.MainViewModel

@Composable
fun Application(
    viewModel: MainViewModel,
    imageLoader: ImageLoader
) {
    val messages = viewModel.getPaginatedData().collectAsLazyPagingItems()
    LazyColumn{
                itemsIndexed(messages){ _, msg ->
                    Text(text = msg?.message ?: "No message")
                }
    }
}
package com.ravnnerdery.chat_challenge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import com.ravnnerdery.chat_challenge.application.MainViewModel
import com.ravnnerdery.chat_challenge.ui.inBox.Inbox

@Composable
fun Application(
    viewModel: MainViewModel,
    imageLoader: ImageLoader,
    sendMessage: (String) -> Unit
) {
    val messages = viewModel.getPaginatedData().collectAsLazyPagingItems()
    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Inbox(
            messages = messages,
            sendMessage = { sendMessage(it) },
        )
    }

}
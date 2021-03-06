package com.ravnnerdery.chat_challenge.ui.inBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.ravnnerdery.chat_challenge.ui.inBox.components.Messages
import com.ravnnerdery.data.database.models.MessageEntity

@Composable
fun Inbox(
    messages: LazyPagingItems<MessageEntity>,
    updateMessageToRead: (String) -> Unit,
    sendMessage: suspend (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Messages(
            messages = messages,
            sendMessage = { sendMessage(it) },
            updateMessageToRead = { updateMessageToRead(it) },
        )
    }
}
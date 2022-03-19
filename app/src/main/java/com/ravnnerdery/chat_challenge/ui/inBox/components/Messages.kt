package com.ravnnerdery.chat_challenge.ui.inBox.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.ravnnerdery.domain.models.Message
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Messages(
    messages: LazyPagingItems<Message>,
    sendMessage: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
        ) {
            val keyboardController: SoftwareKeyboardController? =
                LocalSoftwareKeyboardController.current
            val messageValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
            Row {
                OutlinedTextField(
                    singleLine = true,
                    label = { Text("Message") },
                    value = messageValue.value,
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        sendMessage(messageValue.value)
                    }),
                    onValueChange = { messageValue.value = it }
                )
                IconButton(onClick = { sendMessage(messageValue.value) }) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "",
                        tint = MaterialTheme.colors.error
                    )
                }
            }
            val coroutineScope = rememberCoroutineScope()
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                state = listState
            ) {
                itemsIndexed(messages) { _, msg ->
                    Text(text = "${ msg?.message }, index: ${listState.firstVisibleItemIndex}" ?: "No message", style = MaterialTheme.typography.h5)
                    Divider(color = Color.Red, thickness = 150.dp)
                }
                coroutineScope.launch {
                    listState.scrollToItem(listState.firstVisibleItemIndex+1)
                }
            }
        }
    }
}

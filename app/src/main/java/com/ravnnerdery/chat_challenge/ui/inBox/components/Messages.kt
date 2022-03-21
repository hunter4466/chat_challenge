package com.ravnnerdery.chat_challenge.ui.inBox.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.ravnnerdery.data.database.models.MessageEntity
import kotlinx.coroutines.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Messages(
    messages: LazyPagingItems<MessageEntity>,
    sendMessage: suspend (String) -> Unit,
    updateMessageToRead: (String) -> Unit
) {
    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
        ) {
            val keyboardController: SoftwareKeyboardController? =
                LocalSoftwareKeyboardController.current
            val messageValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedTextField(
                    singleLine = true,
                    label = { Text("Message") },
                    value = messageValue.value,
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        uiScope.launch(Dispatchers.IO) {
                            sendMessage(messageValue.value)
                        }
                    }),
                    onValueChange = { messageValue.value = it },
                    modifier = Modifier.width(320.dp)
                )
                IconButton(onClick = {
                    uiScope.launch(Dispatchers.IO) {
                        sendMessage(messageValue.value)
                        keyboardController?.hide()
                    }
                }) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .height(70.dp)
                            .padding(end = 32.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                reverseLayout = true
            ) {
                itemsIndexed(messages) { _, msg ->
                    SingleMessage(
                        msg = msg,
                        msg_user = msg?.userId ?: "no_user",
                        updateMessageToRead = { updateMessageToRead(it) }
                    )
                }
            }
        }
    }
}

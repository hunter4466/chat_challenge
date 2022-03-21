package com.ravnnerdery.chat_challenge.ui

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.analytics.FirebaseAnalytics
import com.ravnnerdery.chat_challenge.application.MainViewModel
import com.ravnnerdery.chat_challenge.ui.inBox.Inbox
import com.ravnnerdery.domain.other.Constants.FIREBASE_MESSAGE_ID

@Composable
fun Application(
    firebaseAnalytics: FirebaseAnalytics,
    viewModel: MainViewModel,
    sendMessage: suspend (String) -> Unit,
    updateMessageToRead: (String) -> Unit
) {

    val messages = viewModel.getPaginatedData().collectAsLazyPagingItems()
    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Inbox(
            messages = messages,
            sendMessage = {
                sendMessage(it)
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.GROUP_ID, FIREBASE_MESSAGE_ID)
                bundle.putString(FirebaseAnalytics.Param.CONTENT, "Add message to fireStore: $it")
                firebaseAnalytics.logEvent("message_sent", bundle)
            },
            updateMessageToRead = {
                updateMessageToRead(it)
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.GROUP_ID, FIREBASE_MESSAGE_ID)
                bundle.putString(FirebaseAnalytics.Param.CONTENT, "Updated message to read: $it")
                firebaseAnalytics.logEvent("update_message_to_read", bundle)
            }
        )
    }
}
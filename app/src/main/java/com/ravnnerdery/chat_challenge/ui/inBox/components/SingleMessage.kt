package com.ravnnerdery.chat_challenge.ui.inBox.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.domain.other.Constants.FIREBASE_MESSAGE_ID
import com.ravnnerdery.domain.other.timeConvertor

@Composable
fun SingleMessage(
    msg: MessageEntity?,
    msg_user: String,
    updateMessageToRead: (String) -> Unit
) {
    val isNotMessageOwner = msg_user != FIREBASE_MESSAGE_ID
    var arrangement = Arrangement.End
    var endPadding = 8.dp
    var startPadding = 64.dp
    var color = Color(0xFF17E644)
    if (isNotMessageOwner) {
        updateMessageToRead(msg?.messageId ?: "No-id")
        endPadding = 64.dp
        startPadding = 8.dp
        arrangement = Arrangement.Start
        color = Color(0xFFFFFFFF)
    }
    Row(
        horizontalArrangement = arrangement,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            elevation = 8.dp,
            modifier = Modifier.padding(
                start = startPadding,
                end = endPadding,
                top = 4.dp,
                bottom = 4.dp
            ),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = color
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row {
                    Text(
                        text = timeConvertor(msg?.time ?: 1L),
                        style = MaterialTheme.typography.body2
                    )
                }
                Row {
                    Text(
                        text = msg?.message ?: "no-message",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                if (!isNotMessageOwner) {
                    Row {
                        if (msg?.read == true) {
                            Text(text = "Read")
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "",
                                tint = MaterialTheme.colors.onBackground,
                                modifier = Modifier
                                    .height(14.dp)
                                    .padding(start = 4.dp, top = 4.dp)
                            )
                        } else {
                            Text(text = "Unread")

                        }
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "",
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier
                                .height(14.dp)
                                .padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
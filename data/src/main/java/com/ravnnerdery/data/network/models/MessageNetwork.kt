package com.ravnnerdery.data.network.models

data class MessageNetwork(
    val message: String,
    val time: Long,
    val read: Boolean,
    val userId: String,
    val messageId: String,
    val image: String?,
)
package com.ravnnerdery.domain.models

data class Message(
    val message: String,
    val time: Long,
    val read: Boolean,
    val userId: String,
    val messageId : String,
    val image: String?,
)

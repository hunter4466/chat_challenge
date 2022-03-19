package com.ravnnerdery.data.utils

fun randKeyGen(): String {
    val charPool = listOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")
    val labelLength: Int = 10
    val labelArray = mutableListOf<String>()
    for (elm in 1..labelLength){
        labelArray.add(charPool[(charPool.indices).random()])
    }
    return labelArray.joinToString("")
}
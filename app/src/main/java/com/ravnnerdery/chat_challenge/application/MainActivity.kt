package com.ravnnerdery.chat_challenge.application

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.ravnnerdery.chat_challenge.ui.Application
import com.ravnnerdery.chat_challenge.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private fun sendMessage(message: String) = mainViewModel.insertNewMessage(message)

    private fun refreshList(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageLoader = ImageLoader.invoke(this).newBuilder()
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(this@MainActivity))
                } else {
                    add(GifDecoder())
                }
            }.build()
        setContent {
            ApplicationTheme {
                Surface() {
                    Application(
                        viewModel = mainViewModel,
                        imageLoader = imageLoader,
                        sendMessage = { sendMessage(it) })
                }
            }
        }
    }
}
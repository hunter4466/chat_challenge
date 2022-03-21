package com.ravnnerdery.chat_challenge.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import com.google.firebase.analytics.FirebaseAnalytics
import com.ravnnerdery.chat_challenge.ui.Application
import com.ravnnerdery.chat_challenge.ui.theme.ApplicationTheme
import com.ravnnerdery.domain.other.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private suspend fun sendMessage(message: String) = mainViewModel.insertNewMessage(message)
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.GROUP_ID, Constants.FIREBASE_MESSAGE_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, "Chat Application Initialized}")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        setContent {
            ApplicationTheme {
                Surface() {
                    Application(
                        firebaseAnalytics = firebaseAnalytics,
                        viewModel = mainViewModel,
                        sendMessage = { sendMessage(it) },
                        updateMessageToRead = { mainViewModel.updateMessageToRead(it) }
                    )
                }
            }
        }
    }
}
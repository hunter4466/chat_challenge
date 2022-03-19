package com.ravnnerdery.data.network.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ravnnerdery.domain.other.Constants.FIREBASE_COLLECTION
import com.ravnnerdery.domain.other.Constants.FIREBASE_MESSAGE_ID
import com.ravnnerdery.domain.other.Constants.FIRESTORE_TAG
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class FirestoreDbImpl @Inject constructor() : FirestoreDb {
    private val fireStoreDB: FirebaseFirestore = Firebase.firestore
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun addMessage(message: String) {
        val newMessage = hashMapOf(
            "image" to "https://i.picsum.photos/id/391/200/300.jpg?hmac=3xP-y2PRN2E0__aPOCp1sja7XrimKgLQAMiSaNd0Cko",
            "message" to message,
            "time" to System.currentTimeMillis(),
            "read" to false,
            "userId" to FIREBASE_MESSAGE_ID,
        )
        fireStoreDB.collection(FIREBASE_COLLECTION)
            .add(newMessage)
            .addOnSuccessListener { documentReference ->
                Log.d(FIRESTORE_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(FIRESTORE_TAG, "Error adding document", e)
            }
    }

    override suspend fun getFirstMessages(): List<DocumentSnapshot> {
        var newMessagesQuery: Query? = null
        return try {
            newMessagesQuery = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION)
                .orderBy("time", Query.Direction.ASCENDING)
                .whereEqualTo("read", true)
                .limitToLast(10)
            newMessagesQuery.get().await().documents
        }catch (e: Throwable) {
            Log.wtf("MARIOCH", "GET MESSAGES THROWED AN EXCEPTION $e")
            emptyList()
        }
    }

    override suspend fun getMoreMessages(key: Pair<String, Long>?): List<DocumentSnapshot> {
        var newMessagesQuery: Query? = null
        try {
            if (key == null) {
                Log.wtf("MARIOCH","GOT HERE IN IF because key = $key")
                newMessagesQuery = FirebaseFirestore.getInstance()
                    .collection(FIREBASE_COLLECTION)
                    .orderBy("time", Query.Direction.ASCENDING)
                    .whereEqualTo("read", true)
                    .limitToLast(10)
            } else {
                Log.wtf("MARIOCH","GOT HERE IN ELSE because key = $key")
                val keySnapshot: QuerySnapshot = FirebaseFirestore.getInstance().collection(
                    FIREBASE_COLLECTION
                ).orderBy("time", Query.Direction.ASCENDING)
                    .whereEqualTo("time",key.second.toString())
                    .limitToLast(1).get().await()
                when (key.first) {
                    "prepend" -> {
                        newMessagesQuery = FirebaseFirestore.getInstance()
                            .collection(FIREBASE_COLLECTION)
                            .orderBy("time", Query.Direction.ASCENDING)
                            .endBefore(keySnapshot)
                            .limitToLast(10)
                    }
                    "append" -> {
                        newMessagesQuery = FirebaseFirestore.getInstance()
                            .collection(FIREBASE_COLLECTION)
                            .orderBy("time", Query.Direction.ASCENDING)
                            .limit(10)
                            .startAfter(keySnapshot)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.wtf("MARIOCH", "GET MESSAGES THROWED AN EXCEPTION $e")
        }
        return try {newMessagesQuery?.get()?.await()?.documents ?: emptyList()} catch (e: Exception){
            Log.wtf("MARIOCH", "Something happened in request: $e")
            emptyList()
        }
    }

    override fun getMessageEvents(key: Pair<String, String?>?): Flow<List<DocumentSnapshot>> =
        callbackFlow {
            var eventsCollection: Query? = null
            if (key?.second.isNullOrEmpty()) {
                try {
                    eventsCollection = FirebaseFirestore.getInstance()
                        .collection(FIREBASE_COLLECTION)
                        .orderBy("time", Query.Direction.ASCENDING)
                        .whereEqualTo("read", true)
                        .limitToLast(10)
                } catch (e: Throwable) {
                    Log.wtf("MARIOCH","something wrong happened here $e")
                    close(e)
                }
            } else {
                when(key?.first){
                    "prepend" -> {
                        try {
                            Log.wtf("MARIOCH","TRYING TO PULL DATA FROM PREPEND WITH ID: ${key.second}")
                            eventsCollection = FirebaseFirestore.getInstance()
                                .collection(FIREBASE_COLLECTION)
                                .orderBy("time", Query.Direction.ASCENDING)
                                .endBefore(key.second)
                                .limitToLast(10)
                        } catch (e: Throwable) {
                            close(e)
                        }
                    }
                    "append" -> {
                        try {
                            Log.wtf("MARIOCH","TRYING TO PULL DATA FROM APPEND WITH ID: ${key.second}")
                            eventsCollection = FirebaseFirestore.getInstance()
                                .collection(FIREBASE_COLLECTION)
                                .orderBy("time", Query.Direction.ASCENDING)
                                .limit(10)
                                .startAfter(key.second)
                        } catch (e: Throwable) {
                            close(e)
                        }
                    }
                }
            }

            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                // Sends events to the flow! Consumers will get the new events
                try {
                    Log.wtf("MARIOCH","<<<<<<<<<<SOMETHING GOT ADDED TO THE REMOTE DB>>>>>>>>>>>")
                    this.trySend(snapshot.documents).isSuccess
                } catch (e: Throwable) {
                    Log.v(FIRESTORE_TAG, "Error in suscription")
                }
            }
            awaitClose {
                Log.wtf("MARIOCH","SUBSCRIPTION CANCELLED")
                subscription?.remove() }
        }.flowOn(Dispatchers.IO)
}
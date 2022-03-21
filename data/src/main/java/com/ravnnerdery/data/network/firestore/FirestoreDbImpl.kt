package com.ravnnerdery.data.network.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ravnnerdery.domain.other.Constants.FIREBASE_COLLECTION
import com.ravnnerdery.domain.other.Constants.FIREBASE_MESSAGE_ID
import com.ravnnerdery.domain.other.Constants.FIRE_STORE_TAG
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreDbImpl @Inject constructor() : FirestoreDb {
    private val fireStoreDB: FirebaseFirestore = Firebase.firestore


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
                Log.d(FIRE_STORE_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(FIRE_STORE_TAG, "Error adding document", e)
            }
    }

    override suspend fun getAppendMessages(key: String): List<DocumentSnapshot> {
        var newMessagesQuery: Query? = null
        return try {
            val keySnapshot: DocumentSnapshot = FirebaseFirestore.getInstance().collection(
                FIREBASE_COLLECTION
            ).document(key).get().await()
            newMessagesQuery = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION)
                .orderBy("time", Query.Direction.ASCENDING)
                .limitToLast(10)
                .endBefore(keySnapshot)
            newMessagesQuery.get().await().documents
        } catch (e: Throwable) {
            emptyList()
        }
    }

    override suspend fun getFirstMessages(): List<DocumentSnapshot> {
        var newMessagesQuery: Query? = null
        return try {
            newMessagesQuery = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION)
                .orderBy("time", Query.Direction.ASCENDING)
                .limitToLast(10)
            newMessagesQuery.get().await().documents
        }catch (e: Throwable) {
            emptyList()
        }
    }



    override fun getMessageEvents(): Flow<List<DocumentSnapshot>> =
        callbackFlow {
            var eventsCollection: Query? = null
                try {
                    eventsCollection = FirebaseFirestore.getInstance()
                        .collection(FIREBASE_COLLECTION)
                        .orderBy("time", Query.Direction.ASCENDING)
                        .limitToLast(1)
                } catch (e: Throwable) {
                    close(e)
                }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    this.trySend(snapshot.documents).isSuccess
                } catch (e: Throwable) {
                    Log.v(FIRE_STORE_TAG, "Error in suscription")
                }
            }
            awaitClose {
                subscription?.remove() }
        }.flowOn(Dispatchers.IO)

    override fun updateMessageToRead(id: String) {
        val messageRef = fireStoreDB.collection(FIREBASE_COLLECTION).document(id)
        messageRef
            .update("read", true)
            .addOnSuccessListener { Log.d(FIRE_STORE_TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(FIRE_STORE_TAG, "Error updating document", e) }
    }
}
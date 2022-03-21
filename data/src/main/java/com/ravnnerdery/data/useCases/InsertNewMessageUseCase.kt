package com.ravnnerdery.data.useCases

import com.ravnnerdery.data.repository.MainRepository
import javax.inject.Inject

interface InsertNewMessageUseCase {
    suspend fun execute(message: String)
}

class InsertNewMessageUseCaseImpl @Inject constructor(private val repo: MainRepository) :
    InsertNewMessageUseCase {
    override suspend fun execute(message: String) {
        repo.insertNewMessage(message)
    }
}
package com.ravnnerdery.data.useCases

import com.ravnnerdery.domain.repository.MainRepository
import javax.inject.Inject

interface InsertNewMessageUseCase {
    fun execute(message: String)
}

class InsertNewMessageUseCaseImpl @Inject constructor(private val repo: MainRepository): InsertNewMessageUseCase{
    override fun execute(message: String) {
        repo.insertNewMessage(message)
    }
}
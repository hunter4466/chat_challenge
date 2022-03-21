package com.ravnnerdery.data.useCases

import com.ravnnerdery.data.repository.MainRepository
import javax.inject.Inject

interface ProvideUpdateMessageToReadUseCase {
    fun execute(id: String)
}

class ProvideUpdateMessageToReadUseCaseImpl @Inject constructor(private val repo: MainRepository) :
    ProvideUpdateMessageToReadUseCase {
    override fun execute(id: String) {
        repo.updateMessageToRead(id)
    }
}
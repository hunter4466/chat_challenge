package com.ravnnerdery.chat_challenge.application

import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.useCases.InsertNewMessageUseCase
import com.ravnnerdery.data.useCases.ProvidePaginatedDataSourceUseCase
import com.ravnnerdery.data.useCases.ProvideUpdateMessageToReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val providePaginatedDataSourceUseCase: ProvidePaginatedDataSourceUseCase,
    private val insertNewMessageUseCase: InsertNewMessageUseCase,
    private val provideUpdateMessageToReadUseCase: ProvideUpdateMessageToReadUseCase,
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    fun getPaginatedData(): Flow<PagingData<MessageEntity>> =
        providePaginatedDataSourceUseCase.execute()

    suspend fun insertNewMessage(message: String) = insertNewMessageUseCase.execute(message)
    fun updateMessageToRead(id: String) = provideUpdateMessageToReadUseCase.execute(id)

}

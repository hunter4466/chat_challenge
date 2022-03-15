package com.ravnnerdery.chat_challenge.application

import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.ravnnerdery.data.useCases.ProvidePaginatedDataSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val providePaginatedDataSourceUseCase: ProvidePaginatedDataSourceUseCase
) : ViewModel() {
    fun getPaginatedData() = providePaginatedDataSourceUseCase.execute()
}

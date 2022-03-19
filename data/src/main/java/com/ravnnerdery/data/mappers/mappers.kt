package com.ravnnerdery.domain.other

interface DomainMapper<T : Any> {
    fun mapToDomainModel(): T
}
package com.ravnnerdery.data.mappers

interface DomainMapper<T : Any> {
    fun mapToDomainModel(): T
}
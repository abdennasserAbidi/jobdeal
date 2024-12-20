package com.example.myjob.base.reources

/**
 * Represents the resource object that a viewmodel should retourn to Activity in all case possible
 */
open class Resource<out T> constructor(
    val status: ResourceState,
    val data: T?,
    val message: String?
) {
    fun <T> success(data: T): Resource<T> = Resource(ResourceState.SUCCESS, data, null)

    fun <T> error(message: String): Resource<T> = Resource(ResourceState.ERROR, null, message)

    fun <T> loading(): Resource<T> = Resource(ResourceState.LOADING, null, null)
}

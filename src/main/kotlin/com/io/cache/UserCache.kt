package com.io.cache

interface UserCache {

    fun saveUser(userId: String): Boolean

    fun updateStateUser(userId: String)
}
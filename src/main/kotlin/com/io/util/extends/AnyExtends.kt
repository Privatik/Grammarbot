package com.io.util.extends

import kotlin.random.Random

fun<T> List<T>.getRandomItemOrNull(): T?{
    if (isEmpty()) return null

    return getOrNull(Random.nextInt(size))
}
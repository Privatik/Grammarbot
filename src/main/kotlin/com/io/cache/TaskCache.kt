package com.io.cache

import com.io.cache.entity.TaskEntity

interface TaskCache {

    suspend fun getRandomTaskFromSection(sectionId: Long): TaskEntity
}
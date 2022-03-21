package com.io.cache.impl

import com.io.cache.TaskCache
import com.io.cache.entity.TaskEntity

class TaskCacheImpl: TaskCache {

    override suspend fun getRandomTaskFromSection(sectionId: Long): TaskEntity {
        TODO("Not yet implemented")
    }
}
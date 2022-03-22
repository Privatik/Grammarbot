package com.io.service.impl

import com.io.service.TaskCache
import com.io.service.entity.TaskEntity

class TaskCacheImpl: TaskCache {

    override suspend fun getRandomTaskFromSection(sectionId: Long): TaskEntity {
        TODO("Not yet implemented")
    }
}
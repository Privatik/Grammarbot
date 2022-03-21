package com.io.cache

import com.io.cache.entity.TaskEntity

interface TaskCache {

    fun getRandomTaskFromSection(sectionId: Long): TaskEntity

}
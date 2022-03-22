package com.io.service

import com.io.service.entity.TaskEntity

interface TaskCache {

    suspend fun getRandomTaskFromSection(sectionId: Long): TaskEntity

}
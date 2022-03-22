package com.io.service.entity

data class SectionEntity(
    val id: Long,
    val title: String,
)

data class SectionRuleEntity(
    val id: Long,
    val sectionId: Long,
    val ruleEn: String,
    val ruleRu: String
)

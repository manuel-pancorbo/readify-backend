package com.readify.readerlibrary.domain.chapter

interface ChapterRepository {
    fun save(chapter: Chapter)
}
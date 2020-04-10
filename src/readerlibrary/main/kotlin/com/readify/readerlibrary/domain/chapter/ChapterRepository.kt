package com.readify.readerlibrary.domain.chapter

interface ChapterRepository {
    fun save(chapter: Chapter)
    fun findById(chapterId: ChapterId): Chapter?
}
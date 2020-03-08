package com.readify.bookpublishing.domain.chapter

interface ChapterRepository {
    fun save(chapter: Chapter)
}

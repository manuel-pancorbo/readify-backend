package com.readify.readerlibrary.infrastructure.domain.readerlibrary

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryPartialBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibraryRepository
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaLibraryBook
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaLibraryBookType
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaLibraryBookDataSource

class JpaReaderLibraryRepository(private val jpaDataSource: ReaderLibraryJpaLibraryBookDataSource) :
    ReaderLibraryRepository {
    override fun save(readerLibrary: ReaderLibrary) {
        jpaDataSource.saveAll(readerLibrary.toJpa())
    }

    override fun findByReaderId(readerId: ReaderId): ReaderLibrary? {
        return jpaDataSource.findAllByReaderId(readerId.value).toDomain()
    }

}

private fun List<JpaLibraryBook>.toDomain(): ReaderLibrary? {
    if (this.isEmpty()) return null

    return ReaderLibrary(ReaderId(this.first().readerId), map {
        when (it.type) {
            JpaLibraryBookType.WHOLE -> LibraryWholeBook(BookId(it.id))
            JpaLibraryBookType.PARTIAL -> LibraryPartialBook(BookId(it.id), it.chapters.map { id -> ChapterId(id) })
        }
    }.associateBy { it.id })
}

private fun ReaderLibrary.toJpa(): List<JpaLibraryBook> =
    library.values.map {
        when (it) {
            is LibraryPartialBook -> JpaLibraryBook(
                it.bookId.value, readerId.value, it.chapters.map { chapter -> chapter.value },
                JpaLibraryBookType.PARTIAL
            )
            is LibraryWholeBook -> JpaLibraryBook(
                it.bookId.value, readerId.value, emptyList(), JpaLibraryBookType.WHOLE
            )
        }
    }

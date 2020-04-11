package com.readify.readerlibrary.infrastructure.domain.readerlibrary

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.IntegrationTest
import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.readerlibrary.LibraryPartialBook
import com.readify.readerlibrary.domain.readerlibrary.LibraryWholeBook
import com.readify.readerlibrary.domain.readerlibrary.ReaderLibrary
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaLibraryBookDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class JpaReaderLibraryRepositoryShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: JpaReaderLibraryRepository

    @Autowired
    private lateinit var dataSource: ReaderLibraryJpaLibraryBookDataSource

    @BeforeEach
    fun setUp() {
        dataSource.deleteAll()
    }

    @Test
    fun `save a library`() {
        val library = ReaderLibrary(ReaderId(readerId), mapOf(
            BookId(bookId) to LibraryWholeBook(BookId(bookId)),
            BookId(anotherBookId) to LibraryPartialBook(BookId(anotherBookId), listOf(ChapterId(chapterId)))
        ))

        repository.save(library)
        val actualLibrary = repository.findByReaderId(ReaderId(readerId))

        assertThat(actualLibrary).isEqualTo(library)
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val anotherBookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
    }
}
package com.readify.bookpublishing.infrastructure.jpa.chapter

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.readify.IntegrationTest
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.chapter.ChapterId
import com.readify.bookpublishing.domain.chapter.Content
import com.readify.bookpublishing.domain.chapter.DraftChapter
import com.readify.bookpublishing.domain.chapter.Title
import com.readify.bookpublishing.infrastructure.domain.book.JpaChapterRepository
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterDataSource
import com.readify.bookpublishing.infrastructure.jpa.bookpublishing.JpaChapterStatus
import com.readify.shared.domain.clock.Clock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class JpaChapterRepositoryShould : IntegrationTest() {
    @Autowired
    private lateinit var repository: JpaChapterRepository

    @Autowired
    private lateinit var dataSource: JpaChapterDataSource

    @BeforeEach
    fun setUp() {
        dataSource.deleteAll()
    }

    @Test
    fun `save a chapter`() {
        val chapter = anyChapter()

        repository.save(chapter)

        val actual = dataSource.findById(chapter.id.value)
        assertThat(actual.isPresent).isTrue()
        assertThat(actual.get().id).isEqualTo(chapter.id.value)
        assertThat(actual.get().authorId).isEqualTo(chapter.authorId.value)
        assertThat(actual.get().bookId).isEqualTo(chapter.bookId.value)
        assertThat(actual.get().title).isEqualTo(chapter.title.value)
        assertThat(actual.get().content).isEqualTo(chapter.content.value)
        assertThat(actual.get().status).isEqualTo(JpaChapterStatus.DRAFT)
        assertThat(actual.get().modifiedAt).isNotNull()
        assertThat(actual.get().publishedAt).isNull()
    }

    private fun anyChapter() =
        DraftChapter(
            ChapterId(UUID.randomUUID().toString()),
            Title("any title"),
            Content(
                """any
                multiline
                content
            """
            ),
            AuthorId(UUID.randomUUID().toString()),
            BookId(UUID.randomUUID().toString()),
            Clock().now()
        )
}
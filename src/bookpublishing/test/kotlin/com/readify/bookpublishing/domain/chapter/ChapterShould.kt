package com.readify.bookpublishing.domain.chapter

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.readify.shared.domain.event.book.ChapterPublished
import org.junit.jupiter.api.Test

class ChapterShould {
    @Test
    fun `after publishing a draft chapter should be created a domain event`() {
        val draftChapter = ChapterMother().draftOne(ANY_AUTHOR_ID, ANY_BOOK_ID)

        val publishedChapter = draftChapter.publish()

        assertThat(publishedChapter).isInstanceOf(PublishedChapter::class)
        assertThat(publishedChapter.pullDomainEvents()).hasSize(1)
        assertThat(publishedChapter.pullDomainEvents()[0]).isInstanceOf(ChapterPublished::class)
        assertThat((publishedChapter.pullDomainEvents()[0] as ChapterPublished).id).isEqualTo(publishedChapter.id.value)
    }

    companion object {
        private const val ANY_AUTHOR_ID = "f2d2151a-a32a-4c35-a2c8-b53ed8b07908"
        private const val ANY_BOOK_ID = "bf806dec-b2e8-44b1-805b-8db748d1439f"
    }
}
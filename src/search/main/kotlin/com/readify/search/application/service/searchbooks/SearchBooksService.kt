package com.readify.search.application.service.searchbooks

import com.readify.search.application.service.common.ApplicationBook
import com.readify.search.application.service.common.ApplicationBookStatus
import com.readify.search.domain.book.AuthorFilter
import com.readify.search.domain.book.Book
import com.readify.search.domain.book.BookRepository
import com.readify.search.domain.book.BookSearchResult
import com.readify.search.domain.book.BookStatus
import com.readify.search.domain.book.SearchCriteria
import com.readify.search.domain.book.TagFilter
import com.readify.search.domain.book.TextFilter

class SearchBooksService(private val bookRepository: BookRepository) {
    fun execute(request: SearchBooksRequest) =
        bookRepository.search(request.toSearchCriteria())
            .toResponse()
}

data class SearchBooksRequest(val text: String? = null, val tag: String? = null, val authorId: String? = null)
data class SearchBooksResponse(val total: Int, val results: List<ApplicationBook>)

private fun SearchBooksRequest.toSearchCriteria() =
    SearchCriteria(
        text?.let { TextFilter(it) },
        tag?.let { TagFilter(it) },
        authorId?.let { AuthorFilter(it) }
    )

private fun BookSearchResult.toResponse() = SearchBooksResponse(total, results.map { it.toApplicationBook() })

private fun Book.toApplicationBook() = ApplicationBook(bookId.value, authorId.value, title.value, cover.value,
    summary.value, tags.value, price.amount, price.currency.toString(), completionPercentage.value,
    status.toApplicationStatus(), finishedAt)

private fun BookStatus.toApplicationStatus() =
    when (this) {
    BookStatus.IN_PROGRESS -> ApplicationBookStatus.IN_PROGRESS
    BookStatus.FINISHED -> ApplicationBookStatus.FINISHED
}

package com.readify.readerlibrary.infrastructure.eventsubscriber

import com.readify.readerlibrary.application.service.addbooktoreaderlibrary.AddBookToReaderLibraryRequest
import com.readify.readerlibrary.application.service.addbooktoreaderlibrary.AddBookToReaderLibraryService
import com.readify.readerlibrary.application.service.addchaptertoreaderlibrary.AddChapterToReaderLibraryRequest
import com.readify.readerlibrary.application.service.addchaptertoreaderlibrary.AddChapterToReaderLibraryService
import com.readify.shared.domain.event.payment.PaymentCompleted
import com.readify.shared.domain.event.payment.PaymentType
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class PaymentSubscriber(
    private val addBookToReaderLibraryService: AddBookToReaderLibraryService,
    private val addChapterToReaderLibraryService: AddChapterToReaderLibraryService
) {
    @EventListener
    fun on(event: PaymentCompleted) {
        when (event.type) {
            PaymentType.BOOK -> addBookToReaderLibraryService.execute(
                AddBookToReaderLibraryRequest(
                    event.bookId,
                    event.readerId
                )
            )
            PaymentType.CHAPTER -> addChapterToReaderLibraryService.execute(
                AddChapterToReaderLibraryRequest(
                    event.bookId,
                    event.chapterId!!,
                    event.readerId
                )
            )
        }
    }
}
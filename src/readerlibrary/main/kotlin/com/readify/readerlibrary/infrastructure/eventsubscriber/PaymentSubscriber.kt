package com.readify.readerlibrary.infrastructure.eventsubscriber

import com.readify.readerlibrary.application.service.addbooktoreaderlibrary.AddBookToReaderLibraryRequest
import com.readify.readerlibrary.application.service.addbooktoreaderlibrary.AddBookToReaderLibraryService
import com.readify.shared.domain.event.payment.PaymentCompleted
import com.readify.shared.domain.event.payment.PaymentType
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class PaymentSubscriber(private val addBookToReaderLibraryService: AddBookToReaderLibraryService) {
    @EventListener
    fun on(event: PaymentCompleted) {
        when (event.type) {
            PaymentType.BOOK -> addBookToReaderLibraryService.execute(AddBookToReaderLibraryRequest(event.bookId, event.readerId))
            PaymentType.CHAPTER -> TODO()
        }
    }
}
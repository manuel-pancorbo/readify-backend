package com.readify.readerlibrary.domain.readerlibrary

import com.readify.readerlibrary.domain.payment.ReaderId

interface ReaderLibraryRepository {
    fun save(readerLibrary: ReaderLibrary)
    fun findByReaderId(readerId: ReaderId): ReaderLibrary?
}

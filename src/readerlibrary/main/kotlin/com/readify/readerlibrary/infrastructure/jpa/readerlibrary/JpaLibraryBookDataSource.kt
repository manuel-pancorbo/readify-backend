package com.readify.readerlibrary.infrastructure.jpa.readerlibrary

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

interface ReaderLibraryJpaLibraryBookDataSource : JpaRepository<JpaLibraryBook, String> {
    fun findAllByReaderId(readerId: String): List<JpaLibraryBook>
}

@Entity
@Table(name = "librarybook", schema = "readerlibrary")
@TypeDef(name = "list-array", typeClass = ListArrayType::class)
data class JpaLibraryBook(
    @Id
    val id: String,
    val readerId: String,
    @Type(type = "list-array")
    val chapters: List<String>,
    @Enumerated(EnumType.STRING)
    val type: JpaLibraryBookType
)

enum class JpaLibraryBookType { WHOLE, PARTIAL }
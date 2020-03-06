package com.readify.bookpublishing.infrastructure.jpa.bookpublishing

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

interface JpaBookDataSource: JpaRepository<JpaBook, String>

@Entity
@Table(name = "book", schema = "bookpublishing")
@TypeDef(name = "list-array", typeClass = ListArrayType::class)
data class JpaBook(
    @Id
    val id: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    @Type(type = "list-array")
    val tags: List<String>
)
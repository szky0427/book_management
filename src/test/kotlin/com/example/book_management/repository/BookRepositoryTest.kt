package com.example.book_management.repository

import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.dto.response.BookResponseDto
import com.example.book_management.status.PublishStatus
import jooq.Tables.BOOKS
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@JooqTest
@Transactional
@Import(BookRepository::class)
@Sql(scripts = ["/test_data.sql"])
class BookRepositoryTest {
  @Autowired
  lateinit var create: DSLContext

  @Autowired
  lateinit var bookRepository: BookRepository

  @Test
  fun findBooksByAuthorId_正常系_著者IDに紐づく書籍を取得できる() {
    val authorId = 1 // テストデータに存在する著者
    val expect = listOf(
      BookResponseDto(
        1, "世界の歩き方", 1500, PublishStatus.Published.code, PublishStatus.Published.value,
        listOf(
          AuthorResponseDto(1, "相田太郎", LocalDate.of(1975, 5, 20)),
          AuthorResponseDto(2, "川野一郎", LocalDate.of(1980, 11, 12))
        )
      ),
      BookResponseDto(
        4, "世界不思議大全", 1800, PublishStatus.UnPublished.code, PublishStatus.UnPublished.value,
        listOf(AuthorResponseDto(1, "相田太郎", LocalDate.of(1975, 5, 20)))
      )
    )

    val result = bookRepository.findBooksByAuthorId(authorId)

    assertEquals(expect, result)
  }

  @Test
  fun findBooksByAuthorId_正常系_取得件数0の時空のListを返却() {
    val authorId = 100 // テストデータに存在しない著者
    val result = bookRepository.findBooksByAuthorId(authorId)
    assertTrue(result.isEmpty())
  }

  @Test
  fun existsByBookId_正常系_書籍が存在する場合trueを返却() {
    val bookId = 1 // テストデータに存在する書籍ID
    val result = bookRepository.existsByBookId(bookId)
    assertTrue(result)
  }

  @Test
  fun existsByBookId_正常系_書籍が存在しない場合falseを返却() {
    val bookId = 100
    val result = bookRepository.existsByBookId(bookId)
    assertFalse(result)
  }

  @Test
  fun findStatusById_正常系_書籍の出版状況を取得できる() {
    val bookId = 1 // テストデータに存在する書籍
    val result = bookRepository.findStatusById(bookId)
    assertEquals("1", result)
  }

  @Test
  fun findStatusById_異常系_書籍が存在しない場合nullを返却() {
    val bookId = 100 // テストデータに存在しない書籍
    val result = bookRepository.findStatusById(bookId)
    assertEquals(null, result)
  }

  @Test
  fun insertBook_正常系_書籍を登録し書籍IDを返却() {
    val result = bookRepository.insertBook("世界の果物", 2500, PublishStatus.Published.code)

    // 登録した書籍のIDが正しいか確認（テストデータにはID4まで登録済）
    assertEquals(5, result)

    // 登録した書籍が存在するか確認
    val insertedBook = create.selectFrom(BOOKS).where(BOOKS.ID.eq(5)).fetchOne()
    assertEquals("世界の果物", insertedBook!!.title)
    assertEquals(2500, insertedBook.price)
    assertEquals(PublishStatus.Published.code, insertedBook.publishStatus)
  }

  @Test
  fun updateBook_正常系_書籍を更新できる() {
    val bookId = 1 // テストデータに存在する書籍

    bookRepository.updateBook(bookId, "世界の偉人", 1800, PublishStatus.UnPublished.code)

    // 更新内容が反映されているか確認
    val updatedBook = create.selectFrom(BOOKS).where(BOOKS.ID.eq(bookId)).fetchOne()
    assertEquals("世界の偉人", updatedBook!!.title)
    assertEquals(1800, updatedBook.price)
    assertEquals(PublishStatus.UnPublished.code, updatedBook.publishStatus)
  }
}
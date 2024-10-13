package com.example.book_management.repository

import jooq.Tables.BOOK_AUTHORS
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@JooqTest
@Transactional
@Import(BookAuthorsRepository::class)
@Sql(scripts = ["/test_data.sql"])
class BookAuthorsRepositoryTest {
  @Autowired
  lateinit var create: DSLContext

  @Autowired
  lateinit var bookAuthorRepository: BookAuthorsRepository

  @Test
  fun insertBookAuthors_正常系_書籍著者情報を登録できる() {
    val bookId = 2 // テストデータに存在する書籍ID
    val authorId = 4 // テストデータに存在する著者ID

    bookAuthorRepository.insertBookAuthors(bookId, authorId)

    // 書籍著者情報が登録されていることを確認
    val result = create.selectFrom(BOOK_AUTHORS)
      .where(BOOK_AUTHORS.BOOK_ID.eq(bookId)).and(BOOK_AUTHORS.AUTHOR_ID.eq(authorId)).fetchOne()
    assertEquals(bookId, result?.bookId)
    assertEquals(authorId, result?.authorId)
  }

  @Test
  fun deleteBookAuthors_正常系_書籍著者情報を削除できる() {
    val bookId = 1 // テストデータに存在する書籍ID
    bookAuthorRepository.deleteBookAuthors(bookId)

    // 指定した書籍IDのデータが削除されていることを確認
    val result = create.selectFrom(BOOK_AUTHORS)
      .where(BOOK_AUTHORS.BOOK_ID.eq(bookId)).fetch()
    assertTrue(result.isEmpty())
  }
}
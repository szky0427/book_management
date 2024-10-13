package com.example.book_management.repository


import jooq.tables.Authors.AUTHORS
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@JooqTest
@Transactional
@Import(AuthorRepository::class)
@Sql(scripts = ["/test_data.sql"])
class AuthorRepositoryTest {
  @Autowired
  lateinit var create: DSLContext

  @Autowired
  lateinit var authorRepository: AuthorRepository

  @Test
  fun insertAuthor_著者情報を登録できる() {
    val name = "山田太郎"
    val birthDate = LocalDate.of(1990, 3, 15)

    val result = authorRepository.insertAuthor(name, birthDate)

    // 登録した著者IDが正しいか確認（テストデータにはID5まで登録済）
    assertEquals(6, result)

    // 登録した著者が存在することを確認
    val insertedAuthor = create.selectFrom(AUTHORS).where(AUTHORS.ID.eq(result)).fetchOne()
    assertEquals(name, insertedAuthor!!.name)
    assertEquals(birthDate, insertedAuthor.birthDate)
  }

  @Test
  fun updateAuthor_正常系_著者情報の更新ができる() {
    val authorId = 1 // テストデータに存在する著者ID

    authorRepository.updateAuthor(authorId, "加藤綾乃", LocalDate.of(1988, 10, 10))

    // 更新されていることを確認
    val updatedAuthor = create.selectFrom(AUTHORS).where(AUTHORS.ID.eq(authorId)).fetchOne()
    assertEquals("加藤綾乃", updatedAuthor!!.name)
    assertEquals(LocalDate.of(1988, 10, 10), updatedAuthor.birthDate)
  }

  @Test
  fun existsByAuthorId_正常系_著者が存在する場合trueを返却() {
    val authorId = 1 // テストデータに存在する著者ID

    val result = authorRepository.existsByAuthorId(authorId)

    assertEquals(true, result)
  }

  @Test
  fun existsByAuthorId_正常系_著者が存在しない場合falseを返却() {
    val authorId = 100 // テストデータに存在しない著者ID

    val result = authorRepository.existsByAuthorId(authorId)

    assertEquals(false, result)
  }
}
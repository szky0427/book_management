package com.example.book_management.repository

import com.example.book_management.dto.response.AuthorResponseDto
import jooq.Tables.*
import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
著者テーブルを操作するrepositoryクラス
 */
@Repository
class AuthorRepository(private val create: DSLContext) {
  /**
  著者の情報を登録し著者IDを返却するメソッド
   */
  fun insertAuthor(name: String, birthDate: LocalDate): Int {
    val result = create.insertInto(AUTHORS, AUTHORS.NAME, AUTHORS.BIRTH_DATE)
      .values(name, birthDate)
      .returning(AUTHORS.ID)
      .fetchOne()
    return result!!.id
  }

  /**
  著者の情報を更新するメソッド
   */
  fun updateAuthor(authorId: Int, name: String, birthDate: LocalDate) {
    create.update(AUTHORS)
      .set(AUTHORS.NAME, name)
      .set(AUTHORS.BIRTH_DATE, birthDate)
      .where(AUTHORS.ID.eq(authorId))
      .execute()
  }

  /**
   * 著者が存在するか確認するメソッド
   */
  fun existsByAuthorId(authorId: Int): Boolean {
    return create.fetchCount(
      create.selectFrom(AUTHORS).where(AUTHORS.ID.eq(authorId))
    ) > 0
  }

  /**
   * 著者を全件取得するメソッド
   */
  fun selectAll(): List<AuthorResponseDto> {
    val result = create.select(
      AUTHORS.ID,
      AUTHORS.NAME,
      AUTHORS.BIRTH_DATE
    )
      .from(AUTHORS)
      .fetch()

    return result.map { record ->
      AuthorResponseDto(
        id = record.get(AUTHORS.ID),
        name = record.get(AUTHORS.NAME),
        birthDate = record.get(AUTHORS.BIRTH_DATE)
      )
    }
  }

  fun findAuthors(name: String?, authorId: Int?, birthDate: LocalDate?): List<AuthorResponseDto> {
    val result = create
      .select(AUTHORS.ID, AUTHORS.NAME, AUTHORS.BIRTH_DATE)
      .from(AUTHORS)
      .where(name?.let { AUTHORS.NAME.likeIgnoreCase("%$it%") } ?: trueCondition())
        .and(authorId?.let { AUTHORS.ID.eq(it) } ?: trueCondition())
        .and(birthDate?.let { AUTHORS.BIRTH_DATE.eq(it) } ?: trueCondition())
      .orderBy(AUTHORS.ID)
      .fetch()

    // 取得件数0の時、空のListを返却する
    if (result.isEmpty()) return emptyList()

    return result.map { record ->
      AuthorResponseDto(
        id = record.get(AUTHORS.ID),
        name = record.get(AUTHORS.NAME),
        birthDate = record.get(AUTHORS.BIRTH_DATE)
      )
    }
  }

  /**
   * 著者IDに紐づく著者を取得するメソッド
   */
  fun findAuthorById(authorId: Int): AuthorResponseDto? {
    val result = create.select(
      AUTHORS.ID,
      AUTHORS.NAME,
      AUTHORS.BIRTH_DATE
    )
      .from(AUTHORS)
      .where(AUTHORS.ID.eq(authorId))
      .fetchOne()

    return result?.let {
      AuthorResponseDto(
        id = result.get(AUTHORS.ID),
        name = result.get(AUTHORS.NAME),
        birthDate = result.get(AUTHORS.BIRTH_DATE)
      )
    }
  }
}
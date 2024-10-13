package com.example.book_management.repository

import jooq.Tables.AUTHORS
import org.jooq.DSLContext
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
}
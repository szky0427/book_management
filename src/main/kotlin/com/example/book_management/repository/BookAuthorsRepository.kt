package com.example.book_management.repository

import jooq.Tables.BOOK_AUTHORS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 書籍・著者テーブルの操作をするrepositoryクラス
 */
@Repository
class BookAuthorsRepository(private val create: DSLContext) {
    /**
    * 書籍・著者の情報を登録する
    */
    fun insertBookAuthors(bookId: Int, authorId: Int) {
        create.insertInto(BOOK_AUTHORS, BOOK_AUTHORS.BOOK_ID, BOOK_AUTHORS.AUTHOR_ID)
            .values(bookId, authorId)
            .execute()
    }

    /**
     * 書籍・著者の情報を削除する
     */
    fun deleteBookAuthors(bookId: Int) {
        create.delete(BOOK_AUTHORS)
            .where(BOOK_AUTHORS.BOOK_ID.eq(bookId))
            .execute()
    }
}
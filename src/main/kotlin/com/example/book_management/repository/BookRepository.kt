package com.example.book_management.repository

import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.dto.response.BookResponseDto
import com.example.book_management.status.PublishStatus
import jooq.Tables.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository


/**
 * 書籍テーブルを操作をするrepositoryクラス
 */
@Repository
class BookRepository(private val create: DSLContext){
    /**
     * 著者IDに紐づく書籍情報を取得するメソッド
     */
    fun findBooksByAuthorId(authorId: Int): List<BookResponseDto> {
        // 書籍IDをキーとし、書籍IDに紐づく複数の書籍情報（著者情報込み）をMap型で取得する。
        val result = create
            .select(BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.PUBLISH_STATUS,
                AUTHORS.ID, AUTHORS.NAME, AUTHORS.BIRTH_DATE)
            .from(BOOKS)
            .join(BOOK_AUTHORS).on(BOOKS.ID.eq(BOOK_AUTHORS.BOOK_ID))
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOK_AUTHORS.AUTHOR_ID))
            .where(BOOKS.ID.`in`(
                    create.select(BOOKS.ID)
                    .from(BOOKS)
                    .join(BOOK_AUTHORS).on(BOOKS.ID.eq(BOOK_AUTHORS.BOOK_ID))
                    .where(BOOK_AUTHORS.AUTHOR_ID.eq(authorId))))
            .orderBy(BOOKS.ID)
            .fetchGroups(BOOKS.ID)

        // 取得件数0の時、空のListを返却する
        if (result.isEmpty()) return emptyList()

        val booksList = result.map { (bookId, records) ->
            // 書籍情報を取得
            val bookRecord = records.first()
            // 著者情報取得しDTOにマッピングしてList化する
            val authorsList = records.map { record ->
                AuthorResponseDto(record.getValue(AUTHORS.ID),
                    record.getValue(AUTHORS.NAME),
                    record.getValue(AUTHORS.BIRTH_DATE))
            }
            // 返却用DTOにマッピング
            BookResponseDto(bookId,
                bookRecord.getValue(BOOKS.TITLE),
                bookRecord.getValue(BOOKS.PRICE),
                bookRecord.getValue(BOOKS.PUBLISH_STATUS),
                PublishStatus.toValue(bookRecord.getValue(BOOKS.PUBLISH_STATUS)),
                authorsList)
        }
        return booksList
    }

    /**
     * 書籍が存在するか確認するメソッド
     */
    fun existsByBookId(bookId: Int): Boolean{
        return create.fetchCount(
            create.selectFrom(BOOKS).where(BOOKS.ID.eq(bookId))) > 0
    }

    /**
     * 書籍のステータスを取得するメソッド
     */
    fun findStatusById(bookId: Int): String? {
        return create.select(BOOKS.PUBLISH_STATUS)
            .from(BOOKS)
            .where(BOOKS.ID.eq(bookId))
            .fetchOne(BOOKS.PUBLISH_STATUS)
    }

    /**
    書籍の情報を登録し書籍IDを返却するメソッド
     */
    fun insertBook(title: String, price: Int, publishStatus: String): Int {
        val result = create.insertInto(BOOKS, BOOKS.TITLE, BOOKS.PRICE, BOOKS.PUBLISH_STATUS)
            .values(title, price, publishStatus)
            .returning(BOOKS.ID)
            .fetchOne()

        return result?.id ?: throw RuntimeException("書籍情報の登録に失敗しました。")
    }

    /**
    書籍の情報を更新するメソッド
     */
    fun updateBook(bookId: Int, title: String, price: Int, publishStatus: String){
        create.update(BOOKS)
            .set(BOOKS.TITLE, title)
            .set(BOOKS.PRICE, price)
            .set(BOOKS.PUBLISH_STATUS, publishStatus)
            .where(BOOKS.ID.eq(bookId))
            .execute()
    }
}
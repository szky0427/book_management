package com.example.book_management.service

import com.example.book_management.dto.request.BookRequestDto
import com.example.book_management.dto.response.BookResponseDto
import com.example.book_management.repository.AuthorRepository
import com.example.book_management.repository.BookAuthorsRepository
import com.example.book_management.repository.BookRepository
import com.example.book_management.status.PublishStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
  private val bookRepository: BookRepository, private val authorRepository: AuthorRepository,
  private val bookAuthorsRepository: BookAuthorsRepository
) {
  /**
   * 書籍の情報を登録する
   */
  fun createBook(bookRequestDto: BookRequestDto) {
    // 著者テーブルに存在する著者であることを確認
    for (authorId in bookRequestDto.authorsIds) {
      if (!authorRepository.existsByAuthorId(authorId)) throw IllegalArgumentException("この著者は存在しません。")
    }
    // 書籍テーブルに登録し書籍IDを取得
    val bookId = bookRepository.insertBook(bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)

    // 書籍・著者テーブルに登録
    for (authorId in bookRequestDto.authorsIds) {
      bookAuthorsRepository.insertBookAuthors(bookId, authorId)
    }
  }

  /**
   * 書籍の情報を更新する
   */
  fun updateBook(bookId: Int, bookRequestDto: BookRequestDto) {
    // 更新対象の書籍が存在するか確認
    if (!bookRepository.existsByBookId(bookId)) throw IllegalArgumentException("この書籍は存在しません。")
    // 出版済の書籍を未出版に変更しようとしていないか確認
    if (bookRequestDto.publishStatus == PublishStatus.UnPublished.code
      && bookRepository.findStatusById(bookId) == PublishStatus.Published.code
    ) {
      throw IllegalArgumentException("出版済の書籍を未出版に変更できません。")
    }
    // 著者テーブルに存在する著者であることを確認
    for (authorId in bookRequestDto.authorsIds) {
      if (!authorRepository.existsByAuthorId(authorId)) throw IllegalArgumentException("この著者は存在しません。")
    }

    // 書籍の情報を更新
    bookRepository.updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)

    // 既存の書籍・著者の関連を削除
    bookAuthorsRepository.deleteBookAuthors(bookId)
    // 書籍・著者テーブルを更新
    for (authorId in bookRequestDto.authorsIds) {
      bookAuthorsRepository.insertBookAuthors(bookId, authorId)
    }
  }

  /**
   * 検索条件に当てはまる書籍情報を取得する
   */
  fun findBooks(title: String?, bookId: Int?, authorName: String?): List<BookResponseDto> {
    return bookRepository.findBooks(title, bookId, authorName)
  }

  /**
   * 書籍IDに紐づく書籍情報を取得する
   */
  fun findBookByBookId(bookId: Int): BookResponseDto {
    return bookRepository.findBookById(bookId)
  }
}
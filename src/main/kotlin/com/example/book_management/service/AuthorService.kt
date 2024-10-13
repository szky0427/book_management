package com.example.book_management.service

import com.example.book_management.dto.request.AuthorRequestDto
import com.example.book_management.repository.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class AuthorService(private val authorRepository: AuthorRepository) {
  /**
   * 著者の情報を登録するメソッド
   */
  fun createAuthor(authorRequestDto: AuthorRequestDto) {
    // 生年月日が現在の日付より過去であるか確認
    if (!isBeforeToday(authorRequestDto.birthDate)) throw IllegalArgumentException("生年月日は現在の日付より過去である必要があります。")

    authorRepository.insertAuthor(authorRequestDto.name, authorRequestDto.birthDate)
  }

  /**
   * 著者の情報を更新するメソッド
   */
  fun updateAuthor(authorId: Int, authorRequestDto: AuthorRequestDto) {
    // 更新対象の著者が存在するか確認
    if (!authorRepository.existsByAuthorId(authorId)) throw IllegalArgumentException("この著者は登録されていません。")
    // 生年月日が現在の日付より過去であるか確認
    if (!isBeforeToday(authorRequestDto.birthDate)) throw IllegalArgumentException("生年月日は現在の日付より過去である必要があります。")

    authorRepository.updateAuthor(authorId, authorRequestDto.name, authorRequestDto.birthDate)
  }

  /**
   * 日付が今日より過去の日付であることを判定するメソッド
   */
  fun isBeforeToday(date: LocalDate): Boolean {
    return date.isBefore(LocalDate.now())
  }
}
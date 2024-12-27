package com.example.book_management.service

import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.repository.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PulldownService(private val authorRepository: AuthorRepository) {
  /**
   * 全ての著者情報を取得する
   */
  fun selectAllAuthors(): List<AuthorResponseDto> {
    return authorRepository.selectAll()
  }
}
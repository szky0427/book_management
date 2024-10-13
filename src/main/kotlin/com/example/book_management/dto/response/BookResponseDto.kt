package com.example.book_management.dto.response

/**
 * 書籍情報返却用Dtoクラス
 */
data class BookResponseDto(
  val id: Int,
  val title: String,
  val price: Int,
  val publishStatus: String,
  val publishStatusName: String,
  val authors: List<AuthorResponseDto>
)

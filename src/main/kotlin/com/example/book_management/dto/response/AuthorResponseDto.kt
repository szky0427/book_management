package com.example.book_management.dto.response

import java.time.LocalDate

/**
 * 著者情報返却用Dtoクラス
 */
data class AuthorResponseDto(
    val id: Int,
    val name: String,
    val birthDate: LocalDate
)

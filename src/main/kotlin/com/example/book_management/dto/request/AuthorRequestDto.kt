package com.example.book_management.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * 著者情報の登録・更新用リクエストDtoクラス
 */
data class AuthorRequestDto(
    val name: String,
    val birthDate: LocalDate
)

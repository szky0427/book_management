package com.example.book_management.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty


/**
 * 書籍の作成・更新用リクエストDtoクラス
 */
data class BookRequestDto(
    val title: String,
    @field:Min(value = 0, message = "価格は0以上である必要があります")
    val price: Int,
    val publishStatus: String,
    @field:NotEmpty(message = "著者は1人以上必要です")
    val authorsIds: List<Int>
)

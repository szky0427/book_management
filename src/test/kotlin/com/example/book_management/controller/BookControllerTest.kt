package com.example.book_management.controller

import com.example.book_management.dto.request.BookRequestDto
import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.dto.response.BookResponseDto
import com.example.book_management.service.BookService
import com.example.book_management.status.PublishStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@WebMvcTest(BookController::class)
class BookControllerTest {
  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var bookService: BookService

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Test
  fun createBook_正常系_書籍が登録できる() {
    val bookRequestDto = BookRequestDto("世界の料理", 2000, PublishStatus.UnPublished.code, listOf(1, 2))
    doNothing().`when`(bookService).createBook(bookRequestDto)

    mockMvc.perform(
      post("/books/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("書籍情報が登録されました。"))
  }

  @Test
  fun createBook_異常系_書籍が登録できない場合400エラー() {
    val bookRequestDto = BookRequestDto("世界の料理", 2000, PublishStatus.UnPublished.code, listOf(1, 2))
    doThrow(IllegalArgumentException("この著者は存在しません。")).`when`(bookService).createBook(bookRequestDto)

    mockMvc.perform(
      post("/books/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(content().string("書籍情報が登録できませんでした: この著者は存在しません。"))
  }

  @Test
  fun createBook_異常系_価格が0未満の場合400エラー() {
    val bookRequestDto = BookRequestDto("世界の料理", -1, PublishStatus.UnPublished.code, listOf(1, 2))

    mockMvc.perform(
      post("/books/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.price").value("価格は0以上である必要があります"))

  }

  @Test
  fun createBook_正常系_価格が0以上の場合登録できる() {
    val bookRequestDto = BookRequestDto("世界の料理", 0, PublishStatus.UnPublished.code, listOf(1, 2))
    doNothing().`when`(bookService).createBook(bookRequestDto)

    mockMvc.perform(
      post("/books/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("書籍情報が登録されました。"))
  }

  @Test
  fun createBook_異常系_著者が0人の場合400エラー() {
    val bookRequestDto = BookRequestDto("世界の料理", 1000, PublishStatus.UnPublished.code, emptyList())

    mockMvc.perform(
      post("/books/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.authorsIds").value("著者は1人以上必要です"))

  }

  @Test
  fun createBook_正常系_著者が1人以上の場合登録できる() {
    val bookRequestDto = BookRequestDto("世界の料理", 2000, PublishStatus.UnPublished.code, listOf(1))
    doNothing().`when`(bookService).createBook(bookRequestDto)

    mockMvc.perform(
      post("/books/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("書籍情報が登録されました。"))
  }

  @Test
  fun updateBook_正常系_書籍情報を更新できる() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の料理", 2000, PublishStatus.UnPublished.code, listOf(1, 2))
    doNothing().`when`(bookService).updateBook(bookId, bookRequestDto)

    mockMvc.perform(
      put("/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("書籍情報が更新されました。"))

  }

  @Test
  fun updateBook_異常系_書籍情報を更新できなかった場合400エラー() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の料理", 2000, PublishStatus.UnPublished.code, listOf(1, 2))
    doThrow(IllegalArgumentException("この書籍は存在しません。")).`when`(bookService).updateBook(bookId, bookRequestDto)

    mockMvc.perform(
      put("/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(content().string("書籍情報が更新できませんでした: この書籍は存在しません。"))
  }

  @Test
  fun updateBook_異常系_価格が0未満の場合400エラー() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の料理", -1, PublishStatus.UnPublished.code, listOf(1, 2))

    mockMvc.perform(
      put("/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.price").value("価格は0以上である必要があります"))

  }

  @Test
  fun updateBook_正常系_価格が0以上の場合登録できる() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の料理", 0, PublishStatus.UnPublished.code, listOf(1, 2))
    doNothing().`when`(bookService).updateBook(bookId, bookRequestDto)

    mockMvc.perform(
      put("/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("書籍情報が更新されました。"))
  }

  @Test
  fun updateBook_異常系_著者が0人の場合400エラー() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の料理", 1000, PublishStatus.UnPublished.code, emptyList())

    mockMvc.perform(
      put("/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.authorsIds").value("著者は1人以上必要です"))

  }

  @Test
  fun updateBook_正常系_著者が1人以上の場合更新できる() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の料理", 2000, PublishStatus.UnPublished.code, listOf(1))
    doNothing().`when`(bookService).updateBook(bookId, bookRequestDto)

    mockMvc.perform(
      put("/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("書籍情報が更新されました。"))
  }

  @Test
  fun findBooksByAuthorId_正常系_著者に紐づく書籍を取得できる() {
    val authorId = 1
    val bookResponseDto = listOf(
      BookResponseDto(
        1, "世界の歩き方", 1000, PublishStatus.UnPublished.code, PublishStatus.UnPublished.value,
        listOf(
          AuthorResponseDto(1, "山田太郎", LocalDate.of(1990, 12, 25)),
          AuthorResponseDto(2, "佐藤花子", LocalDate.of(2000, 12, 25))
        )
      ),
      BookResponseDto(
        2, "家庭の医学", 3000, PublishStatus.Published.code, PublishStatus.Published.value,
        listOf(AuthorResponseDto(1, "山田太郎", LocalDate.of(1990, 12, 25)))
      )
    )
    `when`(bookService.findBooksByAuthorId(authorId)).thenReturn(bookResponseDto)

    mockMvc.perform(
      get("/books/")
        .param("authorId", authorId.toString())
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isOk)
      .andExpect(content().json(objectMapper.writeValueAsString(bookResponseDto)))
  }
}
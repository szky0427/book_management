package com.example.book_management.controller

import com.example.book_management.dto.request.AuthorRequestDto
import com.example.book_management.service.AuthorService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@WebMvcTest(AuthorController::class)
class AuthorControllerTest {
  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var authorService: AuthorService

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Test
  fun createAuthor_正常系_著者情報を登録する() {
    val authorRequestDto = AuthorRequestDto("山田太郎", LocalDate.of(1990, 12, 31))
    doNothing().`when`(authorService).createAuthor(authorRequestDto)

    mockMvc.perform(
      post("/authors/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authorRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("著者情報が登録されました。"))
  }

  @Test
  fun createAuthor_異常系_著者情報が登録できなかった場合400エラー() {
    val authorRequestDto = AuthorRequestDto("山田太郎", LocalDate.of(1990, 12, 31))
    doThrow(IllegalArgumentException("生年月日は現在の日付より過去である必要があります。")).`when`(authorService)
      .createAuthor(authorRequestDto)

    mockMvc.perform(
      post("/authors/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authorRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(content().string("著者情報が登録できませんでした。: 生年月日は現在の日付より過去である必要があります。"))
  }


  @Test
  fun uppdateAuthor_正常系_著者情報を更新できる() {
    val authorId = 1
    val authorRequestDto = AuthorRequestDto("山田太郎", LocalDate.of(1990, 12, 31))
    doNothing().`when`(authorService).updateAuthor(authorId, authorRequestDto)

    mockMvc.perform(
      put("/authors/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authorRequestDto))
    )
      .andExpect(status().isOk)
      .andExpect(content().string("著者情報が更新されました。"))
  }

  @Test
  fun createAuthor_異常系_著者情報が更新できなかった場合400エラー() {
    val authorId = 1
    val authorRequestDto = AuthorRequestDto("山田太郎", LocalDate.of(1990, 12, 31))
    doThrow(IllegalArgumentException("生年月日は現在の日付より過去である必要があります。")).`when`(authorService)
      .updateAuthor(authorId, authorRequestDto)

    mockMvc.perform(
      put("/authors/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authorRequestDto))
    )
      .andExpect(status().isBadRequest)
      .andExpect(content().string("著者情報が更新できませんでした。: 生年月日は現在の日付より過去である必要があります。"))
  }

}



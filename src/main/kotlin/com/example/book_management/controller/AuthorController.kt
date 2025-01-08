package com.example.book_management.controller

import com.example.book_management.dto.request.AuthorRequestDto
import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService) {
  /**
   * 著者情報を登録するAPI
   */
  @PostMapping("/create")
  fun createAuthor(@RequestBody authorRequestDto: AuthorRequestDto): ResponseEntity<String> {
    return try {
      authorService.createAuthor(authorRequestDto)
      ResponseEntity("著者情報が登録されました。", HttpStatus.OK)
    } catch (e: IllegalArgumentException) {
      ResponseEntity("著者情報が登録できませんでした。: ${e.message}", HttpStatus.BAD_REQUEST)
    }
  }

  /**
   * 著者情報を更新するAPI
   */
  @PutMapping("/{authorId}")
  fun updateAuthor(
    @PathVariable authorId: Int,
    @RequestBody authorRequestDto: AuthorRequestDto
  ): ResponseEntity<String> {
    return try {
      authorService.updateAuthor(authorId, authorRequestDto)
      ResponseEntity("著者情報が更新されました。", HttpStatus.OK)
    } catch (e: IllegalArgumentException) {
      ResponseEntity("著者情報が更新できませんでした。: ${e.message}", HttpStatus.BAD_REQUEST)
    }
  }

  /**
   * 検索条件に合う著者を取得するAPI
   */
  @GetMapping("/")
  fun findAuthors(
    @RequestParam("name") name: String?,
    @RequestParam("authorId") authorId: Int?,
    @RequestParam("birthDate") birthDate: LocalDate?
  ): ResponseEntity<List<AuthorResponseDto>> {
    return ResponseEntity.ok(authorService.findAuthors(name, authorId, birthDate))
  }

  /**
   * 著者IDに紐づく著者を取得するAPI
   */
  @GetMapping("/{authorId}")
  fun findAuthor(@PathVariable authorId: Int): ResponseEntity<AuthorResponseDto> {
    return ResponseEntity.ok(authorService.findAuthorByAuthorId(authorId))
  }
}
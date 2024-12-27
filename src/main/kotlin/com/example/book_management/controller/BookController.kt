package com.example.book_management.controller

import com.example.book_management.dto.request.BookRequestDto
import com.example.book_management.dto.response.BookResponseDto
import com.example.book_management.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {
  /**
   * 書籍を登録するAPI
   */
  @PostMapping("/create")
  fun createBook(@Valid @RequestBody bookRequestDto: BookRequestDto): ResponseEntity<String> {
    return try {
      bookService.createBook(bookRequestDto)
      ResponseEntity("書籍情報が登録されました。", HttpStatus.OK)
    } catch (e: IllegalArgumentException) {
      ResponseEntity("書籍情報が登録できませんでした: ${e.message}", HttpStatus.BAD_REQUEST)
    }
  }

  /**
   * 書籍を更新するAPI
   */
  @PutMapping("/{bookId}")
  fun updateBook(
    @PathVariable bookId: Int,
    @Valid @RequestBody bookRequestDto: BookRequestDto
  ): ResponseEntity<String> {
    return try {
      bookService.updateBook(bookId, bookRequestDto)
      ResponseEntity("書籍情報が更新されました。", HttpStatus.OK)
    } catch (e: IllegalArgumentException) {
      ResponseEntity("書籍情報が更新できませんでした: ${e.message}", HttpStatus.BAD_REQUEST)
    }
  }

  /**
   * 検索条件に合う書籍を取得するAPI
   */
  @GetMapping("/")
  fun findBooks(
    @RequestParam("title") title: String?,
    @RequestParam("bookId") bookId: Int?,
    @RequestParam("authorName") authorName: String?
  ): ResponseEntity<List<BookResponseDto>> {
    return ResponseEntity.ok(bookService.findBooks(title, bookId, authorName))

  }

  /**
   * 書籍IDに紐づく書籍を取得するAPI
   */
  @GetMapping("/{bookId}")
  fun findBooks(@PathVariable bookId: Int): ResponseEntity<BookResponseDto> {
    return ResponseEntity.ok(bookService.findBookByBookId(bookId))
  }
}
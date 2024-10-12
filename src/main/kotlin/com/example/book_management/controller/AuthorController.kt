package com.example.book_management.controller

import com.example.book_management.dto.request.AuthorRequestDto
import com.example.book_management.service.AuthorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService)  {
    /**
     * 著者情報を登録するAPI
     */
    @PostMapping("/create")
     fun createAuthor (@RequestBody authorRequestDto: AuthorRequestDto) :ResponseEntity<String> {
        return try {
            authorService.createAuthor(authorRequestDto)
            ResponseEntity("著者情報が登録されました。", HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity("著者情報が登録できませんでした: ${e.message}", HttpStatus.BAD_REQUEST)
        }
     }

    /**
     * 著者情報を更新するAPI
     */
    @PutMapping("/{authorId}")
    fun updateAuthor(@PathVariable authorId: Int,
                   @RequestBody authorRequestDto: AuthorRequestDto) : ResponseEntity<String> {
        return try {
            authorService.updateAuthor(authorId, authorRequestDto)
            ResponseEntity("著者情報が更新されました。", HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity("著者情報が更新できませんでした: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }
}
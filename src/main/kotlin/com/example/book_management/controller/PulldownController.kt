package com.example.book_management.controller

import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.service.PulldownService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pulldown")
class PulldownController(private val pulldownService: PulldownService) {
  /**
   * 書籍IDに紐づく書籍を取得するAPI
   */
  @GetMapping("/authors")
  fun findAuthors(): ResponseEntity<List<AuthorResponseDto>> {
    return ResponseEntity.ok(pulldownService.selectAllAuthors())
  }
}
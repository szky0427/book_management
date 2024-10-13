package com.example.book_management.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  /**
   * バリデーションエラーの場合400エラーを返却する
   */
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
    val errors: MutableMap<String, String> = HashMap()
    ex.bindingResult.allErrors.forEach { error ->
      val fieldName = (error as org.springframework.validation.FieldError).field
      val errorMessage = error.defaultMessage ?: "Invalid value"
      errors[fieldName] = errorMessage
    }
    return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
  }
}
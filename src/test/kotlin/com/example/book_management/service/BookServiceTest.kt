package com.example.book_management.service

import com.example.book_management.dto.request.BookRequestDto
import com.example.book_management.dto.response.AuthorResponseDto
import com.example.book_management.dto.response.BookResponseDto
import com.example.book_management.repository.AuthorRepository
import com.example.book_management.repository.BookAuthorsRepository
import com.example.book_management.repository.BookRepository
import com.example.book_management.status.PublishStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class BookServiceTest {
  @Mock
  private lateinit var bookRepository: BookRepository

  @Mock
  private lateinit var bookAuthorsRepository: BookAuthorsRepository

  @Mock
  private lateinit var authorRepository: AuthorRepository

  @InjectMocks
  private lateinit var bookService: BookService

  @BeforeEach
  fun setUp() {
    MockitoAnnotations.openMocks(this)
  }

  @Test
  fun createBook_正常系_書籍を登録できる() {
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.UnPublished.code, listOf(1, 2))
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(2)).thenReturn(true)
    `when`(bookRepository.insertBook(bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)).thenReturn(1)

    bookService.createBook(bookRequestDto)

    verify(bookRepository, times(1)).insertBook(
      bookRequestDto.title,
      bookRequestDto.price,
      bookRequestDto.publishStatus
    )
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(1, 1)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(1, 2)
  }

  @Test
  fun createBook_異常系_存在しない著者の場合例外発生() {
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.UnPublished.code, listOf(1, 3))
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(3)).thenReturn(false)

    val exception = assertThrows<IllegalArgumentException> {
      bookService.createBook(bookRequestDto)
    }

    verify(bookRepository, times(0)).insertBook(anyString(), anyInt(), anyString())
    verify(bookAuthorsRepository, times(0)).insertBookAuthors(anyInt(), anyInt())
    assertEquals("この著者は存在しません。", exception.message)
  }

  @Test
  fun updateBook_正常系_書籍を更新できる() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.UnPublished.code, listOf(1, 2))
    `when`(bookRepository.existsByBookId(1)).thenReturn(true)
    `when`(bookRepository.findStatusById(bookId)).thenReturn(PublishStatus.UnPublished.code)
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(2)).thenReturn(true)

    bookService.updateBook(bookId, bookRequestDto)

    verify(bookRepository, times(1)).updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)
    verify(bookAuthorsRepository, times(1)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 1)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 2)
  }

  @Test
  fun updateBook_異常系_存在しない書籍の場合例外発生() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.UnPublished.code, listOf(1, 3))
    `when`(bookRepository.existsByBookId(1)).thenReturn(false)

    val exception = assertThrows<IllegalArgumentException> {
      bookService.updateBook(bookId, bookRequestDto)
    }

    verify(bookRepository, times(1)).existsByBookId(bookId)
    verify(bookRepository, times(0)).updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)
    verify(bookAuthorsRepository, times(0)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(0)).insertBookAuthors(anyInt(), anyInt())
    assertEquals("この書籍は存在しません。", exception.message)
  }

  @Test
  fun updateBook_異常系_出版済から未出版に更新する場合例外発生() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.UnPublished.code, listOf(1, 2))
    `when`(bookRepository.existsByBookId(1)).thenReturn(true)
    `when`(bookRepository.findStatusById(bookId)).thenReturn(PublishStatus.Published.code)

    val exception = assertThrows<IllegalArgumentException> {
      bookService.updateBook(bookId, bookRequestDto)
    }

    verify(bookRepository, times(0)).updateBook(
      bookId,
      bookRequestDto.title,
      bookRequestDto.price,
      bookRequestDto.publishStatus
    )
    verify(bookAuthorsRepository, times(0)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(0)).insertBookAuthors(anyInt(), anyInt())
    assertEquals("出版済の書籍を未出版に変更できません。", exception.message)
  }

  @Test
  fun updateBook_正常系_未出版から出版済に更新() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.Published.code, listOf(1, 2))
    `when`(bookRepository.existsByBookId(1)).thenReturn(true)
    `when`(bookRepository.findStatusById(bookId)).thenReturn(PublishStatus.UnPublished.code)
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(2)).thenReturn(true)

    bookService.updateBook(bookId, bookRequestDto)

    verify(bookRepository, times(1)).updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)
    verify(bookAuthorsRepository, times(1)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 1)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 2)
  }

  @Test
  fun updateBook_正常系_未出版から未出版に更新() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.UnPublished.code, listOf(1, 2))
    `when`(bookRepository.existsByBookId(1)).thenReturn(true)
    `when`(bookRepository.findStatusById(bookId)).thenReturn(PublishStatus.UnPublished.code)
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(2)).thenReturn(true)

    bookService.updateBook(bookId, bookRequestDto)

    verify(bookRepository, times(1)).updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)
    verify(bookAuthorsRepository, times(1)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 1)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 2)
  }

  @Test
  fun updateBook_正常系_出版済から出版済に更新() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.Published.code, listOf(1, 2))
    `when`(bookRepository.existsByBookId(1)).thenReturn(true)
    `when`(bookRepository.findStatusById(bookId)).thenReturn(PublishStatus.Published.code)
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(2)).thenReturn(true)

    bookService.updateBook(bookId, bookRequestDto)

    verify(bookRepository, times(1)).updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)
    verify(bookAuthorsRepository, times(1)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 1)
    verify(bookAuthorsRepository, times(1)).insertBookAuthors(bookId, 2)
  }

  @Test
  fun updateBook_異常系_存在しない著者の場合例外発生() {
    val bookId = 1
    val bookRequestDto = BookRequestDto("世界の歩き方", 1, PublishStatus.Published.code, listOf(1, 3))
    `when`(bookRepository.existsByBookId(1)).thenReturn(true)
    `when`(bookRepository.findStatusById(bookId)).thenReturn(PublishStatus.Published.code)
    `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)
    `when`(authorRepository.existsByAuthorId(2)).thenReturn(false)

    val exception = assertThrows<IllegalArgumentException> {
      bookService.updateBook(bookId, bookRequestDto)
    }

    verify(bookRepository, times(0)).updateBook(bookId, bookRequestDto.title, bookRequestDto.price, bookRequestDto.publishStatus)
    verify(bookAuthorsRepository, times(0)).deleteBookAuthors(bookId)
    verify(bookAuthorsRepository, times(0)).insertBookAuthors(bookId, 1)
    verify(bookAuthorsRepository, times(0)).insertBookAuthors(bookId, 3)
    assertEquals("この著者は存在しません。", exception.message)
  }

  @Test
  fun findBooksByAuthorId_正常系_著者に紐づく書籍を取得() {
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
    `when`(bookRepository.findBooksByAuthorId(authorId)).thenReturn(bookResponseDto)

    val result = bookService.findBooksByAuthorId(authorId)

    assertEquals(bookResponseDto, result)
  }

  fun findBooksByAuthorId_正常系_取得件数0件の時は空のリストを返却() {
    val authorId = 2
    val bookResponseDto = emptyList<BookResponseDto>()
    `when`(bookRepository.findBooksByAuthorId(authorId)).thenReturn(emptyList<BookResponseDto>())

    val result = bookService.findBooksByAuthorId(authorId)

    assertEquals(bookResponseDto, result)
  }

}


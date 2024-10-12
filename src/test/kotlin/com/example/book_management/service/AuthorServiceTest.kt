package com.example.book_management.service

import com.example.book_management.dto.request.AuthorRequestDto
import com.example.book_management.repository.AuthorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.time.LocalDate

class AuthorServiceTest{
    @Mock
    private lateinit var authorRepository: AuthorRepository
    @InjectMocks
    private lateinit var authorService: AuthorService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun createAuthor_正常系_著者情報を登録できる(){
        val authorRequestDto = AuthorRequestDto("渡辺一郎", LocalDate.of(1998, 6, 10))

        authorService.createAuthor(authorRequestDto)

        verify(authorRepository, times(1)).insertAuthor(authorRequestDto.name, authorRequestDto.birthDate)
    }

    @Test
    fun createAuthor_正常系_生年月日が昨日の日付(){
        val authorRequestDto = AuthorRequestDto("渡辺二郎", LocalDate.now().minusDays(1))

        authorService.createAuthor(authorRequestDto)

        verify(authorRepository, times(1)).insertAuthor(authorRequestDto.name, authorRequestDto.birthDate)
    }

    @Test
    fun createAuthor_異常系_生年月日が今日の日付の場合例外発生(){
        val authorRequestDto = AuthorRequestDto("渡辺三郎", LocalDate.now())

        val exception = assertThrows<IllegalArgumentException> {
            authorService.createAuthor(authorRequestDto)
        }

        verify(authorRepository, times(0)).insertAuthor(authorRequestDto.name, authorRequestDto.birthDate)
        assertEquals("生年月日は現在の日付より過去である必要があります。", exception.message)
    }

    @Test
    fun createAuthor_異常系_生年月日が明日の日付の場合例外発生(){
        val authorRequestDto = AuthorRequestDto("渡辺四郎", LocalDate.now().plusDays(1))

        val exception = assertThrows<IllegalArgumentException> {
            authorService.createAuthor(authorRequestDto)
        }

            verify(authorRepository, times(0)).insertAuthor(authorRequestDto.name, authorRequestDto.birthDate)
            assertEquals("生年月日は現在の日付より過去である必要があります。", exception.message)
        }

    @Test
    fun updateAuthor_正常系_著者情報を更新できる() {
        val authorId = 1
        val authorRequestDto = AuthorRequestDto("渡辺一郎", LocalDate.of(1998, 6, 10))
        `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)

        authorService.updateAuthor(authorId, authorRequestDto)

        verify(authorRepository, times(1)).updateAuthor(authorId, authorRequestDto.name, authorRequestDto.birthDate)
    }

    @Test
    fun updateAuthor_異常系_存在しない著者の場合例外発生() {
        val authorId = 2
        val authorRequestDto = AuthorRequestDto("渡辺五郎", LocalDate.of(1998, 6, 10))
        `when`(authorRepository.existsByAuthorId(1)).thenReturn(false)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.updateAuthor(authorId, authorRequestDto)
        }

        verify(authorRepository, times(0)).updateAuthor(authorId, authorRequestDto.name, authorRequestDto.birthDate)
        assertEquals("この著者は登録されていません。", exception.message)
    }

    @Test
    fun updateAuthor_正常系_生年月日が昨日の日付() {
        val authorId = 1
        val authorRequestDto = AuthorRequestDto("渡辺二郎", LocalDate.now().minusDays(1))

        `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)

        authorService.updateAuthor(authorId, authorRequestDto)

        verify(authorRepository, times(1)).updateAuthor(authorId, authorRequestDto.name, authorRequestDto.birthDate)
    }

    @Test
    fun updateAuthor_異常系_生年月日が今日の日付の場合例外発生() {
        val authorId = 1
        val authorRequestDto = AuthorRequestDto("渡辺三郎", LocalDate.now())

        `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.updateAuthor(authorId, authorRequestDto)
        }

        verify(authorRepository, times(0)).updateAuthor(authorId, authorRequestDto.name, authorRequestDto.birthDate)
        assertEquals("生年月日は現在の日付より過去である必要があります。", exception.message)
    }

    @Test
    fun updateAuthor_異常系_生年月日が今日の明日の場合例外発生() {
        val authorId = 1
        val authorRequestDto = AuthorRequestDto("渡辺四郎", LocalDate.now().plusDays(1))

        `when`(authorRepository.existsByAuthorId(1)).thenReturn(true)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.updateAuthor(authorId, authorRequestDto)
        }

        verify(authorRepository, times(0)).updateAuthor(authorId, authorRequestDto.name, authorRequestDto.birthDate)
        assertEquals("生年月日は現在の日付より過去である必要があります。", exception.message)
    }

    @Test
    fun isBeforeToday_正常系_引数の日付が昨日の場合trueを返却(){
        val date = LocalDate.now().minusDays(1)

        val result = authorService.isBeforeToday(date)

        assertEquals(true, result)
    }

    @Test
    fun isBeforeToday_異常系_引数の日付が今日の場合falseを返却(){
        val date = LocalDate.now()

        val result = authorService.isBeforeToday(date)

        assertEquals(false, result)
    }

    @Test
    fun isBeforeToday_異常系_引数の日付が明日の場合falseを返却(){
        val date = LocalDate.now().plusDays(1)

        val result = authorService.isBeforeToday(date)

        assertEquals(false, result)
    }

}
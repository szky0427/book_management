package com.example.book_management.status

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PublishStatusTest {
  @Test
  fun 出版状況のコード値を取得できる() {
    assertEquals("0", PublishStatus.UnPublished.code)
    assertEquals("1", PublishStatus.Published.code)
  }

  @Test
  fun 出版状況の名称を取得できる() {
    assertEquals("未出版", PublishStatus.UnPublished.value)
    assertEquals("出版済", PublishStatus.Published.value)
  }

  @Test
  fun コード値からステータス名を返却する() {
    assertEquals("未出版", PublishStatus.toValue("0"))
    assertEquals("出版済", PublishStatus.toValue("1"))
  }

  @Test
  fun 存在しないコード値の場合は入力値を返却する() {
    assertEquals("99", PublishStatus.toValue("99"))
  }
}
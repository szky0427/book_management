package com.example.book_management.status

/**
出版状況のコード値管理をするenum
 */
enum class PublishStatus(val code: String, val value: String) {
  UnPublished("0", "未出版"),
  Published("1", "出版済");

  // コード値からステータス名を返却する
  companion object {
    fun toValue(code: String): String {
      for (item in enumValues<PublishStatus>()) {
        if (item.code == code) {
          return item.value
        }
      }
      // 該当するものがない場合は入力値をそのまま返却
      return code
    }
  }
}
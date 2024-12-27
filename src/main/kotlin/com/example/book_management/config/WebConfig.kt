package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class WebMvcConfig {
  @Bean
  fun corsFilter(): CorsFilter {
    val source = UrlBasedCorsConfigurationSource()
    val config = CorsConfiguration().apply {
      allowedOrigins = listOf("http://localhost:5173") // フロントエンドのURL
      allowedHeaders = listOf("*")
      allowedMethods = listOf("*")
    }
    source.registerCorsConfiguration("/**", config)
    return CorsFilter(source)
  }
}


package com.grocery.jumarket.configuration


import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {

    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder().group("springjumarketsystem-public")
            .pathsToMatch("/api/carrinho/**", "/api/categorias/**", "/api/produtos/**", "/api/usuarios/**",
            "/api/venda/**").build()
    }

}


package com.example.productprovider

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ProductGateway {

    private val webClient = WebClient.builder().build()

    fun getProductBy(productId: String): Mono<Product> {
        return webClient.get()
            .uri("http://localhost:9002/products/$productId")
            .exchangeToMono {
                it.bodyToMono(Product::class.java)
            }
    }
}

data class Product(val id: String, val name: String)
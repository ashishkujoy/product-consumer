package com.example.productprovider

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor("PService", hostInterface = "localhost", port = "9002")
class ProductGatewayTest {

    @Pact(provider = "PService", consumer = "UConsumer")
    fun createProductByIdPact(builder: PactDslWithProvider): RequestResponsePact {
        val body = PactDslJsonBody()
            .stringType("id", "123")
            .stringType("name", "123-pro")
            .stringType("version", "0.1")

        return builder
            .given("product for given id is present in system")
            .uponReceiving("a request for given product id")
            .path("/products/123")
            .method("GET")
            .willRespondWith()
            .body(body)
            .status(200)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "createProductByIdPact")
    fun `get product for given product id`(mockServer: MockServer) {
        ProductGateway().getProductBy("123").block()
    }
}
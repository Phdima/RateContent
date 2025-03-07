package com.example.ratecontent

import com.example.ratecontent.data.api.BookApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class BookApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var bookApiService: BookApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        bookApiService = retrofit.create(BookApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `searchBooks returns valid data`() = runBlocking {

        val mockResponse = MockResponse()
            .setBody("""
                {
                    "totalItems": 1,
                    "items": [
                        {
                            "id": "123",
                            "volumeInfo": {
                                "title": "Clean Code",
                                "authors": ["Robert Martin"],
                                "description": "Книга о чистом коде",
                                "averageRating": 4.5,
                                "imageLinks": {
                                    "thumbnail": "http://example.com/clean-code.jpg"
                                }
                            }
                        }
                    ]
                }
            """.trimIndent())
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)


        val response = bookApiService.searchBooks("Clean Code")


        assert(response.totalItems == 1)
        assert(response.items?.size == 1)

        val firstItem = response.items?.get(0)
        assert(firstItem?.id == "123")
        assert(firstItem?.volumeInfo?.title == "Clean Code")
        assert(firstItem?.volumeInfo?.authors?.contains("Robert Martin") == true)
    }

    @Test
    fun `searchBooks includes correct query parameters`() = runBlocking {

        val mockResponse = MockResponse()
            .setBody(""" {"totalItems": 0, "items": []} """)
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)

        bookApiService.searchBooks("Kotlin", language = "en", maxResults = 10)


        val request = mockWebServer.takeRequest()
        assert(request.path == "/volumes?q=Kotlin&langRestrict=en&maxResults=10")
    }

    @Test
    fun `searchBooks handles empty response`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody(""" {"totalItems": 0, "items": []} """)
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)

        val response = bookApiService.searchBooks("Unknown Book")
        assert(response.items?.isEmpty() == true)
    }
}
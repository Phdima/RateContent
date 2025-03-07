package com.example.ratecontent

import com.example.ratecontent.data.api.GamesAPIService
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

class GameAPITest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var gamesAPIService: GamesAPIService


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

        gamesAPIService = retrofit.create(GamesAPIService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `searchGames returns valid data`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("""
                {
                    "results": [
                        {
                            "id": 123,
                            "name": "Witcher 3",
                            "background_image": "http://example.com/witcher.jpg",
                            "released": "19.05.2015",
                            "metacritic": 98
                        }
                    ]
                }
            """.trimIndent()
            ).setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = gamesAPIService.searchGames("Witcher 3")

        assert(response.results.size == 1)

        val firstItem = response.results.get(0)
        assert(firstItem.id == 123)
        assert(firstItem.name == "Witcher 3")
        assert(firstItem.metacritic == 98)
        assert(firstItem.released == "19.05.2015")
        assert(firstItem.background_image == "http://example.com/witcher.jpg")
    }

    @Test
    fun `searchGames includes correct parameters`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody("{ \"results\": [] }"))

        gamesAPIService.searchGames("Witcher 3", apiKey = "123")

        val request = mockWebServer.takeRequest()
        assert(request.path?.contains("123") == true)
    }

}
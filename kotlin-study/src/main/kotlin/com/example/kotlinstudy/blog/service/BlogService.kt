package com.example.kotlinstudy.blog.service

import com.example.kotlinstudy.blog.core.exception.InvalidInputException
import com.example.kotlinstudy.blog.dto.BlogDto
import com.example.kotlinstudy.blog.entity.Wordcount
import com.example.kotlinstudy.blog.repository.WordRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.net.http.HttpHeaders

@Service
class BlogService (
    val wordRepository: WordRepository
        ){

    @Value("\${REST_API_KEY}")
    lateinit var restAPiKey: String

    fun searchKakao(blogDto: BlogDto) :String?{

        val webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
            .build()


        /**
         * curl -v -X GET "https://dapi.kakao.com/v2/search/blog" \
        --data-urlencode "query=https://brunch.co.kr/@tourism 집짓기" \
        -H "Authorization: KakaoAK ${REST_API_KEY}"
         */
        val response = webClient
            .get()
            .uri { it.path("v2/search/blog")
                .queryParam("query",blogDto.query)
                .queryParam("sort",blogDto.sort)
                .queryParam("page",blogDto.page)
                .queryParam("size",blogDto.size)
                .build()
            }
            .header("Authorization","KakaoAK ${restAPiKey}")
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()

        //인기 검색어 top10
        val lowQuery: String = blogDto.query.lowercase()
        val word:Wordcount = wordRepository.findById(lowQuery).orElse(Wordcount(lowQuery))

        word.cnt++;
        wordRepository.save(word)

        return result;

    }

    fun searchWordRank():List<Wordcount> = wordRepository.findTop10ByOrderByCntDesc()
}


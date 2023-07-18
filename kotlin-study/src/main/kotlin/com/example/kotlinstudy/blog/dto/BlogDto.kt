package com.example.kotlinstudy.blog.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BlogDto(

    @field:NotBlank(message = "query param required")
    @JsonProperty("query")
    private val _query:String?,

    @field:NotBlank(message = "sort param required")
    val sort:String?,

    @field:Min(1, message = "page is less than min")
    val page:Int?,

    @field:NotNull(message = "size param required")
    val size:Int?,


){
    val query: String
        get() = _query!!
}

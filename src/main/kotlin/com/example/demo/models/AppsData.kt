package com.example.demo.models

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document(collection = "appstatistics")
data class AppsData (@Field("id") val id : String, val reportTime : Date, val type : Int,
                     val videoRequests : Int = 0, val webViewRequests : Int = 0,
                     val videoClicks : Int = 0, val webviewClicks : Int = 0,
                     val videoInstalls : Int = 0, val webviewInstalls : Int = 0){

    var _id : String? = null
}
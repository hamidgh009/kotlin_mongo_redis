package com.example.demo.models

import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document
data class AppStatistics(val year : Int, val weekNum: Int,
                          var requests: Int = 0, var clicks: Int = 0, var installs: Int = 0) : Serializable{
}
package com.example.demo.models

import java.io.Serializable

class AppStatisticsListResponse : Serializable{

    var stats = listOf<AppStatistics>()
        set(value) {field = value.sortedWith(compareBy ({ it.year }, {it.weekNum}))}

}
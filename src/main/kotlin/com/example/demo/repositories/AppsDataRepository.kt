package com.example.demo.repositories

import com.example.demo.models.AppsData
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface AppsDataRepository : MongoRepository <AppsData, String> {
    fun findByType(type : Int) : List<AppsData>

    fun findByTypeAndReportTimeBetween(type: Int, from : Date, to : Date) : List<AppsData>
}
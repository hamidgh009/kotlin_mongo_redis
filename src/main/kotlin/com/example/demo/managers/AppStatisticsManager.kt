package com.example.demo.managers

import com.example.demo.models.AppStatistics
import com.example.demo.models.AppStatisticsListResponse
import com.example.demo.models.AppsData
import com.example.demo.repositories.AppsDataRepository
import com.example.demo.utils.PersianCalendarUtils
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors
import javax.swing.Spring
import kotlin.collections.ArrayList


@Component
class AppStatisticsManager {

    @Autowired
    lateinit var appsDataRepository: AppsDataRepository

    @Cacheable("appResults", key = "#startDate.toString().concat(#endDate.toString()).concat(T(java.lang.String).valueOf(#type))")
    fun calculateAppStatistics(startDate: Date, endDate: Date, type: Int): AppStatisticsListResponse {
        val ans = AppStatisticsListResponse()

        val appsData = appsDataRepository.findByTypeAndReportTimeBetween(type, startDate, endDate)
        ans.stats = aggregateWithPersianYearAndWeek(appsData)

        return ans
    }


    /*
     * @desc aggregate received AppsData list by persian year and week number
     * @input List of AppsData
     * @output List of created AppStatistics
     */

    fun aggregateWithPersianYearAndWeek(appsDataList: List<AppsData>): ArrayList<AppStatistics> {

        val result = appsDataList.groupBy { PersianCalendarUtils.getYear(it.reportTime).toString() + PersianCalendarUtils.getWeekOfYear(it.reportTime).toString() }
                .mapValues {
                    it.value.map {
                        AppStatistics(PersianCalendarUtils.getYear(it.reportTime), PersianCalendarUtils.getWeekOfYear(it.reportTime),
                                it.videoRequests + it.webViewRequests, it.videoClicks+ it.webviewClicks,
                                it.videoInstalls+ it.webviewInstalls)
                    }.reduce { acc, appStatistics ->  AppStatistics(appStatistics.year, appStatistics.weekNum,
                            acc.requests + appStatistics.requests,
                            acc.clicks + appStatistics.clicks , acc.installs + appStatistics.installs)}
        }.values

        return ArrayList(result)
    }

}
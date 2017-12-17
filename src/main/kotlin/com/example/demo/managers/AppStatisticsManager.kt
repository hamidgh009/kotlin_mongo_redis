package com.example.demo.managers

import com.example.demo.models.AppStatistics
import com.example.demo.models.AppStatisticsListResponse
import com.example.demo.models.AppsData
import com.example.demo.repositories.AppsDataRepository
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.ArrayList


@Component
class AppStatisticsManager {

    @Autowired
    val appsDataRepository : AppsDataRepository? = null

    @Cacheable("appResults" , key = "#startDate.toString().concat(#endDate.toString()).concat(T(java.lang.String).valueOf(#type))")
    fun calculateAppStatistics(startDate: Date, endDate: Date, type : Int) : AppStatisticsListResponse {
        val ans = AppStatisticsListResponse()

        val appsData = appsDataRepository!!.findByTypeAndReportTimeBetween(type, startDate, endDate)
        ans.stats = aggregateWithPersianYearAndWeek(appsData)

        return ans
    }


    /*
     * @desc aggregate received AppsData list by persian year and week number
     * @input List of AppsData
     * @output List of created AppStatistics
     * TODO: move calendar configurations to a seperate common file if you need to extend application
     */

    fun aggregateWithPersianYearAndWeek(appsData: List<AppsData>) : ArrayList<AppStatistics>{
        val result = hashMapOf<String, AppStatistics>()
        val locale = ULocale("@calendar=persian")

        for(appData in appsData){
            val calendar = Calendar.getInstance(locale)
            calendar.firstDayOfWeek = 7
            calendar.time = appData.reportTime
            val year = calendar.get(Calendar.YEAR)
            val weekOfYear = if(calendar.get(Calendar.YEAR_WOY) == year) calendar.get(Calendar.WEEK_OF_YEAR) else 53

            val key = year.toString() + weekOfYear.toString()

            if(!result.containsKey(key)) {
                result[key] = AppStatistics(year , weekOfYear)
            }

            result.get(key)!!.requests += appData.videoRequests + appData.webViewRequests
            result.get(key)!!.clicks += appData.videoClicks + appData.webviewClicks
            result.get(key)!!.installs += appData.videoInstalls + appData.webviewInstalls

        }
        return ArrayList<AppStatistics>(result.values)
    }

}
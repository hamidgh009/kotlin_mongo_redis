package com.example.demo.unitTests

import com.example.demo.KotlinApplication
import com.example.demo.managers.AppStatisticsManager
import com.example.demo.models.AppStatistics
import com.example.demo.models.AppsData
import com.example.demo.repositories.AppsDataRepository
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.GregorianCalendar
import com.ibm.icu.util.ULocale
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(KotlinApplication::class)])
class AppStatisticsManagerTest {

    @TestConfiguration
    class AppStatisticsManagerITestConfiguration {
        @Bean
        fun appStatisticsManager(): AppStatisticsManager {
            return AppStatisticsManager()
        }
    }


    @Autowired
    val appStatisticsManager: AppStatisticsManager? = null


    @MockBean
    val repo: AppsDataRepository? = null

    @Autowired
    val redisTemplate : RedisTemplate<String, String>? = null

    /*
     *   @desc Unit test for aggregateWithPersianYearAndWeek method in AppStatisticsManager
    */

    @Test
    fun aggregateWithPersianYearAndWeekTest() {
        /*
         * Null Input test
         */
        var res = appStatisticsManager!!.aggregateWithPersianYearAndWeek(listOf<AppsData>())
        Assert.assertNotNull("Null returned by the method", res)
        Assert.assertEquals("method returned non-empty list by empty input", 0, res.size)

        /*
         * Normal Input test
         */
        val locale = ULocale("@calendar=persian")
        val calendar = Calendar.getInstance(locale)

        val appsDataList = arrayListOf<AppsData>()

        /*first Day of year*/

        calendar.set(1396, 0, 1)
        appsDataList.add(AppsData("1", calendar.time, 1, 1, 1, 1, 1, 1, 1))

        /* last day of leap year */
        calendar.set(1395, 11, 30)
        appsDataList.add(AppsData("1", calendar.time, 2, 2, 2, 2, 2, 2, 2))

        /* first day of normal week */
        calendar.set(1396, 0, 5)
        appsDataList.add(AppsData("1", calendar.time, 5, 5, 5, 5, 5, 5, 5))

        /* last day of normal week */
        calendar.set(1396, 0, 18)
        appsDataList.add(AppsData("1", calendar.time, 9, 9, 9, 9, 9, 9, 9))

        res = appStatisticsManager!!.aggregateWithPersianYearAndWeek(appsDataList)
        Assert.assertNotNull("Null returned by the method", res)
        Assert.assertEquals("normal test result check", 4, res.size)
        Assert.assertTrue(res.contains(AppStatistics(1395,53,4,4,4)))
        Assert.assertTrue(res.contains(AppStatistics(1396,1,2,2,2)))
        Assert.assertTrue(res.contains(AppStatistics(1396,2,10,10,10)))
        Assert.assertTrue(res.contains(AppStatistics(1396,3,18,18,18)))
    }


    /*
     *  @desc Tests Redis Cache. Just run calculateAppStatistics method 2 times, and check if repository calls once.
     */
    @Test
    fun redisTest() {

        /* REQUEST CONFIGURATION */

        val ans = arrayListOf<AppsData>()
        val locale = ULocale("@calendar=persian")
        val calendar = Calendar.getInstance(locale)
        val type = 1

        calendar.set(1395, 11, 30)
        ans.add(AppsData("1", calendar.time, type, 1, 1, 5, 1, 1, 1))

        val startDate = GregorianCalendar(2015, 10, 10).time
        val endDate = GregorianCalendar(2018, 10, 10).time


        Mockito.`when`(repo!!.findByTypeAndReportTimeBetween(type, startDate, endDate))
                .thenReturn(ans)

        redisTemplate!!.delete(startDate.toString()+ endDate.toString() + type.toString())
        /* TEST */
        appStatisticsManager!!.calculateAppStatistics(startDate, endDate, type)

        Mockito.verify(repo, Mockito.times(1))!!.findByTypeAndReportTimeBetween(type, startDate, endDate)

        appStatisticsManager!!.calculateAppStatistics(startDate, endDate, type)

        Mockito.verify(repo, Mockito.times(1))!!.findByTypeAndReportTimeBetween(type, startDate, endDate)

        redisTemplate!!.delete(startDate.toString()+ endDate.toString() + type.toString())

    }
}
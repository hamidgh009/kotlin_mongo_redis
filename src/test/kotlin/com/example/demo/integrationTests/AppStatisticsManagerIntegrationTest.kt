package com.example.demo.integrationTests

import com.example.demo.KotlinApplication
import com.example.demo.managers.AppStatisticsManager
import com.example.demo.models.AppStatistics
import com.example.demo.models.AppStatisticsListResponse
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
class AppStatisticsManagerIntegrationTest {

    @TestConfiguration
    class AppStatisticsManagerITestConfiguration {
        @Bean
        fun appStatisticsManager() : AppStatisticsManager{
            return AppStatisticsManager()
        }
    }

    @Autowired
    lateinit var appStatisticsManager : AppStatisticsManager

    @MockBean
    lateinit var repo : AppsDataRepository

    @Autowired
    lateinit var redisTemplate : RedisTemplate<String, String>


    /*
     * @desc Test calculateAppStatistics method in AppStatisticsManager.
     */
    @Test
    fun calculateAppStatisticsTest(){

        /* REQUEST CONFIGURATION */
        val startDate = GregorianCalendar(2015,10,10).time
        val endDate = GregorianCalendar(2018,10,10).time
        val type = 1

        val ans = arrayListOf<AppsData>()
        val locale = ULocale("@calendar=persian")
        val calendar = Calendar.getInstance(locale)

        calendar.set(1395,11,30)
        ans.add(AppsData("1" , calendar.time, type,1,1,5,1,1,1))

        calendar.set(1396,0,1)
        ans.add(AppsData("1" , calendar.time, type,1,5,1,1,1,1))

        calendar.set(1396,0,4)
        ans.add(AppsData("1" , calendar.time, type,5,1,1,1,5,1))




        Mockito.`when`(repo.findByTypeAndReportTimeBetween(type, startDate, endDate))
                .thenReturn(ans)

        /* RESPONSE CONFIGURATION */

        val response = AppStatisticsListResponse()

        val appStatistics = ArrayList<AppStatistics>()
        appStatistics.add(AppStatistics(1395,53,2,6,2))
        appStatistics.add(AppStatistics(1396,1,12,4,8))
        response.stats = appStatistics

        /* REMOVE KEY FROM CACHE */
        redisTemplate.delete(startDate.toString()+ endDate.toString() + type.toString())

        /* TEST */
        val ret = appStatisticsManager.calculateAppStatistics(startDate, endDate,1)

        Assert.assertNotNull(ret)
        Assert.assertNotNull(ret.stats.get(0))
        Assert.assertNotNull(ret.stats.get(1))
        Assert.assertEquals(response.stats.get(0), ret.stats.get(0))
        Assert.assertEquals(response.stats.get(1), ret.stats.get(1))
    }

}
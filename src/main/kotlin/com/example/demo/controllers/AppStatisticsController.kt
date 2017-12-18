package com.example.demo.controllers

import com.example.demo.exceptions.BadRequestException
import com.example.demo.managers.AppStatisticsManager
import com.example.demo.models.AppStatisticsListResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/stats"])
class AppStatisticsController {

    @Autowired
    private lateinit var appStatisticsManager: AppStatisticsManager


    /*
     * @desc SAMPLE REQUEST: http://127.0.0.1:8080/stats?startDate=1991-10-10&endDate=2020-10-10&type=1
     */
    @RequestMapping(method = [(RequestMethod.GET)])
    @ResponseBody
    fun getStats(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: Date,
                 @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: Date,
                 @RequestParam(value = "type") type: Int): AppStatisticsListResponse {

        if (!areInputsValid(startDate, endDate, type))
            throw BadRequestException()

        return appStatisticsManager.calculateAppStatistics(startDate, endDate, type)
    }

    /*
     * @desc validate all input rules.
     */
    private fun areInputsValid(startDate: Date, endDate: Date, type: Int): Boolean {
        return startDate.before(endDate)
    }
}
package com.example.demo.restTests

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatsTest {

    @Autowired
    val restTemplate : TestRestTemplate? = null

    var headers : HttpHeaders = HttpHeaders()

    @Before
    fun setup(){

    }

    /*
     * @desc Tests rest service for invalid date inputs
     */
    @Test
    fun invalidDateTest(){

        val response = restTemplate!!.exchange(getInvalidDateUrl(), HttpMethod.GET, HttpEntity<String>(headers),String::class.java)
        Assert.assertEquals(400 , response.statusCode.value())
    }

    private fun getInvalidDateUrl(): URI {
        return URI.create("/stats?startDate=1991-10-10&endDate=1020-10-10&type=1")
    }

    /*
     * @desc Tests rest service for invalid url rules
    */
    @Test
    fun invalidUrlTextTest(){
        var response = restTemplate!!.exchange(getInvalidUrlText1(), HttpMethod.GET, HttpEntity<String>(headers),String::class.java)
        Assert.assertEquals(400 , response.statusCode.value())

        response = restTemplate!!.exchange(getInvalidUrlText2(), HttpMethod.GET, HttpEntity<String>(headers),String::class.java)
        Assert.assertEquals(400 , response.statusCode.value())
    }

    private fun getInvalidUrlText1(): URI {
        return URI.create("/stats?startDate=1991-10-10&endDate=2020-10-10&type=notInt")
    }

    private fun getInvalidUrlText2(): URI {
        return URI.create("/stats?startDate=1991-10-10&endDate=2020/10/10&type=1")
    }

    /*
     * @desc Tests rest service for normal request and expect http 200 response
     */
    @Test
    fun normalInputTest(){
        val response = restTemplate!!.exchange(getNormalUri(), HttpMethod.GET, HttpEntity<String>(headers),String::class.java)
        Assert.assertEquals(200 , response.statusCode.value())
    }

    private fun getNormalUri(): URI{
        return URI.create("/stats?startDate=1991-10-10&endDate=2020-10-10&type=1")
    }

}
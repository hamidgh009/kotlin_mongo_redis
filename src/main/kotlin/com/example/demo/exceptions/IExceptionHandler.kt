package com.example.demo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
@RestController
class IExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(e: Exception): String? {
        return "Internal Server Error"
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [(MethodArgumentTypeMismatchException::class)])
    fun handleMethodArgumentTypeMismatchException(e : MethodArgumentTypeMismatchException): String? {
        return "Bad Request"
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [(BadRequestException::class)])
    fun handleBadRequestException(e : BadRequestException): String? {
        return "Bad Request"
    }
}
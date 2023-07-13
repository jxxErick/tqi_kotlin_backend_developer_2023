package com.grocery.jumarket.exception

import com.grocery.jumarket.service.exception.BusinessException
import com.grocery.jumarket.service.exception.NotFoundException
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val messageError = error.defaultMessage
            errors[fieldName] = messageError
        }

        val exceptionDetails = ExceptionDetails(
                title = "Bad Request",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                details = errors
        )

        return ResponseEntity(exceptionDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        val exceptionDetails = ExceptionDetails(
                title = "Conflict",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message)
        )

        return ResponseEntity(exceptionDetails, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ExceptionDetails> {
        val exceptionDetails = ExceptionDetails(
                title = "Bad Request",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message)
        )

        return ResponseEntity(exceptionDetails, HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(NotFoundException::class)
    fun handleCategoriaNotFoundException(ex: NotFoundException): ResponseEntity<ExceptionDetails> {
        val exceptionDetails = ExceptionDetails(
                title = "Não encontrado!",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                exception = ex.javaClass.toString(),
                details = mutableMapOf("message" to ex.message)
        )

        return ResponseEntity(exceptionDetails, HttpStatus.NOT_FOUND)
    }

}

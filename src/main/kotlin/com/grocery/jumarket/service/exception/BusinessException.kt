package com.grocery.jumarket.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class BusinessException(override val message: String?) : RuntimeException()
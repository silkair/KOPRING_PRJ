package io.jin.backend.exeptions

import io.jin.backend.dto.GeneralResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
private class MemberExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException::class)
    fun noSuchElementException(
        e: MemberNotFoundException
    ): GeneralResponse<Void> {
        return GeneralResponse(
            data = null,
            message = e.message!!
        )
    }

}
package org.ckxnhat.resource.exception;

import jakarta.validation.ConstraintViolationException;
import org.ckxnhat.resource.config.i18n.LocaleHolder;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.viewmodel.response.ErrorApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 21:29:41.455
 */

@ControllerAdvice
public class ApiExceptionHandler {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleHolder localeHolder;
    @ExceptionHandler({BadRequestException.class, DuplicatedException.class})
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorApiResponse(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        messageSource.getMessage(e.getMessage(), e.getArgs(),localeHolder.getCurrentLocale())
                ));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorApiResponse(
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        messageSource.getMessage(e.getMessage(), e.getArgs(),localeHolder.getCurrentLocale())
                ));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorApiResponse(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        messageSource.getMessage(ErrorCodeConstant.REQUEST_INFORMATION_NOT_VALID,null,localeHolder.getCurrentLocale())
                ));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorApiResponse(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        messageSource.getMessage(ErrorCodeConstant.REQUEST_INFORMATION_NOT_VALID,null,localeHolder.getCurrentLocale())
                ));
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorApiResponse(
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        messageSource.getMessage(ErrorCodeConstant.RESPONSE_FORBIDDEN,null,localeHolder.getCurrentLocale())
                ));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorApiResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        e.getMessage()
                ));
    }
}

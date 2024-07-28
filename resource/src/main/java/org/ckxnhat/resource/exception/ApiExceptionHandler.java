package org.ckxnhat.resource.exception;

import org.aspectj.weaver.ast.Not;
import org.ckxnhat.resource.config.i18n.LocaleHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

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
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return null;
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return null;
    }
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<?> handleDuplicatedException(DuplicatedException ex){return null;}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return null;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception ex, WebRequest request) {return null;}
}

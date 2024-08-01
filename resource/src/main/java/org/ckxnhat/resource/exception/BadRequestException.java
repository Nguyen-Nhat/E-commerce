package org.ckxnhat.resource.exception;

import lombok.Getter;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 22:59:19.356
 */

@Getter
public class BadRequestException extends RuntimeException {
    private final String message;
    private final Object[] args;
    public BadRequestException(String message, Object... args) {
        this.message = message;
        this.args = args;
    }
}

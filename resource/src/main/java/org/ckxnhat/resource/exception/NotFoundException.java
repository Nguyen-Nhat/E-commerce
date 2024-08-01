package org.ckxnhat.resource.exception;

import lombok.Getter;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 23:01:18.145
 */

@Getter
public class NotFoundException extends RuntimeException {
    private final String message;
    private final Object[] args;
    public NotFoundException(String message, Object... args) {
        this.message = message;
        this.args = args;
    }
}

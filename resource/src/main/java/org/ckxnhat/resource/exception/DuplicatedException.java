package org.ckxnhat.resource.exception;

import lombok.Getter;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 22:58:59.750
 */

@Getter
public class DuplicatedException extends RuntimeException {
    private final String message;
    private final Object[] args;
    public DuplicatedException(String message, Object... args) {
        this.message = message;
        this.args = args;
    }
}

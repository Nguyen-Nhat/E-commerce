package org.ckxnhat.resource.viewmodel.response;

import java.util.Date;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-27 00:06:19.348
 */

public record SuccessApiResponse(String status, Date timestamp, Object data) {
    public SuccessApiResponse(String status, Object data){
        this(status, new Date(), data);
    }
}
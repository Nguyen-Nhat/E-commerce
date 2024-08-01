package org.ckxnhat.resource.viewmodel.response;

import java.util.Date;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-27 00:24:31.958
 */

public record ErrorApiResponse(String status, Date timestamp, String detail) {
    public ErrorApiResponse(String status, String detail){
        this(status, new Date(), detail);
    }
}

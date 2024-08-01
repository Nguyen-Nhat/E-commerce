package org.ckxnhat.bff.viewmodel.response;

import jdk.jfr.DataAmount;
import lombok.*;

import java.util.Date;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 09:15:19.533
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessApiResponse {
    private String status;
    private final Date timestamp = new Date();
    private Object data;
}

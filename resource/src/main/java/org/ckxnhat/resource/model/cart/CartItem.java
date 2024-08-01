package org.ckxnhat.resource.model.cart;

import lombok.Builder;
import lombok.Data;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 21:34:22.903
 */
@Data
@Builder
public class CartItem {
    private Long skuId;
    private Long quantity;
}

package org.ckxnhat.resource.model.cart;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 21:32:31.874
 */
@Data
@Builder
public class Cart {
    private Long userId;
    @Singular
    private Set<CartItem> items = new HashSet<>();
}

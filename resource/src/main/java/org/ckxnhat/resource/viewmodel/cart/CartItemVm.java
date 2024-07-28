package org.ckxnhat.resource.viewmodel.cart;

import jakarta.validation.constraints.Min;
import org.ckxnhat.resource.model.cart.CartItem;

public record CartItemVm(@Min(1)long skuId,@Min(1) long quantity) {
    public static CartItemVm fromModel(CartItem item){
        return new CartItemVm(item.getSkuId(), item.getQuantity());
    }
}

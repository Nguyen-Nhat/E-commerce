package org.ckxnhat.resource.service.cart;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.cart.Cart;
import org.ckxnhat.resource.model.cart.CartItem;
import org.ckxnhat.resource.repository.cart.CartRepository;
import org.ckxnhat.resource.repository.product.SkuRepository;
import org.ckxnhat.resource.util.JwtUtil;
import org.ckxnhat.resource.viewmodel.cart.CartItemVm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-26 22:50:46.044
 */

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final SkuRepository skuRepository;
    public List<CartItemVm> addCartItem(CartItemVm itemVm) {
//        skuRepository.findById(itemVm.skuId())
//                .orElseThrow(() -> new NotFoundException("sku not found"));
        Long userId = JwtUtil.getUserId();
        cartRepository.addItemToCart(userId, CartItem.builder()
                .skuId(itemVm.skuId())
                .quantity(itemVm.quantity())
                .build()
        );
        return getCartItems();
    }
    public List<CartItemVm> incrementCartItem(CartItemVm itemVm){
        Long userId = JwtUtil.getUserId();
        if(!cartRepository.existsBySkuId(userId, itemVm.skuId())){
            throw new NotFoundException("cart item ko ton tai");
        }
        cartRepository.incrementItemQuantity(userId, CartItem.builder()
                .skuId(itemVm.skuId())
                .quantity(itemVm.quantity())
                .build()
        );
        return getCartItems();
    }
    public List<CartItemVm> decrementCartItem(CartItemVm itemVm){
        Long userId = JwtUtil.getUserId();
        if(!cartRepository.existsBySkuId(userId, itemVm.skuId())){
            throw new NotFoundException("cart item ko ton tai");
        }
        cartRepository.decrementItemQuantity(userId, CartItem.builder()
                .skuId(itemVm.skuId())
                .quantity(itemVm.quantity())
                .build()
        );
        return getCartItems();
    }
    public List<CartItemVm> updateCart(List<CartItemVm> cartVm){
        try {

            Long userId = JwtUtil.getUserId();
            Cart cart = Cart.builder()
                    .userId(userId)
                    .items(cartVm.stream()
                            .map(i -> CartItem.builder()
                                    .quantity(i.quantity())
                                    .skuId(i.skuId())
                                    .build()
                            )
                            .collect(Collectors.toSet())
                    ).build();
            cartRepository.updateCart(cart);
            return getCartItems();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<CartItemVm> getCartItems(){
        Long userId = JwtUtil.getUserId();
        Cart cart = cartRepository.getCart(userId);
        return cart.getItems().stream().map(CartItemVm::fromModel).collect(Collectors.toList());
    }
    public void removeCartItem(Long skuId){
        Long userId = JwtUtil.getUserId();
        cartRepository.removeItemFromCart(userId, skuId);
    }
    public void removeListCartItem(List<Long> skuIds){
        Long userId = JwtUtil.getUserId();
        cartRepository.removeListItemFromCart(userId, skuIds);
    }
    public void clearCart(){
        Long userId = JwtUtil.getUserId();
        cartRepository.clearCartItems(userId);
    }
    public Long countCartItem(){
        Long userId = JwtUtil.getUserId();
        return cartRepository.countCartItem(userId);
    }
}

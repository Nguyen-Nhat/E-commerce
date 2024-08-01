package org.ckxnhat.resource.repository.cart;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.model.cart.Cart;
import org.ckxnhat.resource.model.cart.CartItem;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-27 18:08:42.507
 */

@Repository
@RequiredArgsConstructor
public class CartRepository {
    private final HashOperations<String, String, String> hashOps;
    private final RedisTemplate<String,String> redisTemplate;
    private final static String CART_KEY = "cart";
    private String getKey(Long uid) {
        return CART_KEY + ":" + uid;
    }
    public void addItemToCart(Long userId, CartItem item) {
        String value = hashOps.get(getKey(userId), item.getSkuId().toString());
        if(value != null){
            hashOps.increment(getKey(userId), item.getSkuId().toString(),item.getQuantity());
        }
        else {
            hashOps.put(getKey(userId), item.getSkuId().toString(), item.getQuantity().toString());
        }
    }
    public void incrementItemQuantity(Long userId, CartItem item) {
        String value = hashOps.get(getKey(userId), item.getSkuId().toString());
        if(value != null){
            hashOps.increment(getKey(userId), item.getSkuId().toString(),item.getQuantity());
        }
    }
    public void decrementItemQuantity(Long userId, CartItem item) {
        String value = hashOps.get(getKey(userId), item.getSkuId().toString());
        if(value != null){
            long v = Long.parseLong(value);
            if(v > item.getQuantity()){
                hashOps.increment(getKey(userId), item.getSkuId().toString(),-item.getQuantity());
            }
            else {
                hashOps.delete(getKey(userId), item.getSkuId().toString());
            }
        }
    }
    public Set<CartItem> getCartItems(Long userId) {
        Map<String, String> entries = hashOps.entries(getKey(userId));
        return entries.entrySet().stream().map(
                entry -> CartItem.builder()
                        .skuId(Long.valueOf(entry.getKey()))
                        .quantity(Long.valueOf(entry.getValue()))
                        .build()
        ).collect(Collectors.toSet());
    }
    public Cart getCart(Long userId){
        Map<String, String> entries = hashOps.entries(getKey(userId));
        return Cart.builder()
                .userId(userId)
                .items(entries.entrySet().stream().map(
                        entry -> CartItem.builder()
                                .skuId(Long.valueOf(entry.getKey()))
                                .quantity(Long.valueOf(entry.getValue()))
                                .build()
                ).collect(Collectors.toSet()))
                .build();
    }
    public void updateCart(Cart cart){
        redisTemplate.delete(getKey(cart.getUserId()));
        Map<String, String> entries = cart.getItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getSkuId().toString(),
                        item -> item.getQuantity().toString())
                );
        hashOps.putAll(getKey(cart.getUserId()), entries);
    }
    public void removeItemFromCart(Long userId, Long skuId) {
        hashOps.delete(getKey(userId), skuId.toString());
    }
    public void removeListItemFromCart(Long userId, List<Long> skuIds){
        List<String> skuIdStrings = skuIds.stream()
                .map(String::valueOf)
                .toList();

        hashOps.delete(getKey(userId), (Object[]) skuIdStrings.toArray(new String[0]));
    }
    public void clearCartItems(Long userId) {
        // hash type doesn't have delete cml
        redisTemplate.delete(getKey(userId));
    }
    public boolean existsBySkuId(Long userId, Long skuId){
        return hashOps.get(userId.toString(), skuId.toString()) == null;
    }
    public Long countCartItem(Long userId){
        return hashOps.size(getKey(userId));
    }
}

package org.ckxnhat.resource.controller.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.service.cart.CartService;
import org.ckxnhat.resource.viewmodel.cart.CartItemVm;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-28 10:27:58.427
 */

@Controller
@Validated
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping("/")
    public ResponseEntity<?> getCart(){
        List<CartItemVm> data = cartService.getCartItems();
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/count")
    public ResponseEntity<?> getNumberOfItem(){
        Long data = cartService.countCartItem();
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addCartItem(@RequestBody CartItemVm item){
        List<CartItemVm> data = cartService.addCartItem(item);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping("/increment")
    public ResponseEntity<?> incrementCartItem(@RequestBody CartItemVm item){
        List<CartItemVm> data = cartService.incrementCartItem(item);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping("/decrement")
    public ResponseEntity<?> decrementCartItem(@RequestBody CartItemVm item){
        List<CartItemVm> data = cartService.decrementCartItem(item);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
//    @PutMapping("/update")
//    public ResponseEntity<?> update(@RequestBody List<CartItemVm> cartItemVms){
//        List<CartItemVm> data = cartService.updateCart(cartItemVms);
//        return ResponseEntity.ok(new SuccessApiResponse(
//                HttpStatus.OK.getReasonPhrase(),
//                data
//        ));
//    }
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeCartItem(@Min(1) @RequestParam long skuId){
        cartService.removeCartItem(skuId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/multi-remove")
    public ResponseEntity<?> removeListCartItem(@Min(1) @RequestParam List<Long> skuIds){
        cartService.removeListCartItem(skuIds);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(){
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}

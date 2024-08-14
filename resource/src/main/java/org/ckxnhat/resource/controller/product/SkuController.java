package org.ckxnhat.resource.controller.product;

import com.opencsv.bean.validators.PreAssignmentValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.service.product.SkuService;
import org.ckxnhat.resource.viewmodel.product.SkuPostVm;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-13 19:50:09.244
 */

@RestController
@RequestMapping("/sku")
@RequiredArgsConstructor
@Validated
public class SkuController {
    private final SkuService skuService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getSkuById(
            @Min(1) @PathVariable("id") Long id
    ) {
        var data = skuService.getSkuById(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/gtin/{gtin}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getSkuByGtin(
        @NotBlank @PathVariable("gtin") String gtin
    ){
        var data = skuService.getSkuByGtin(gtin);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSku(@Valid @RequestBody SkuPostVm skuPostVm, UriComponentsBuilder ucBuilder){
        var data = skuService.create(skuPostVm);
        return ResponseEntity.created(
                        ucBuilder.replacePath("/sku/{id}").buildAndExpand(data.id()).toUri()
                )
                .body(new SuccessApiResponse(
                        HttpStatus.CREATED.getReasonPhrase(),
                        data
                ));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSku(
            @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody SkuPostVm skuPostVm
    ){
        var data = skuService.update(id, skuPostVm);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
}

package org.ckxnhat.resource.controller.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.PageConstant;
import org.ckxnhat.resource.service.product.BrandService;
import org.ckxnhat.resource.viewmodel.product.BrandGetVm;
import org.ckxnhat.resource.viewmodel.product.BrandListGetVm;
import org.ckxnhat.resource.viewmodel.product.BrandPostVm;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-01 13:26:44.596
 */

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@Validated
public class BrandController {
    private final BrandService brandService;
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getBrands(
            @RequestParam(name = PageConstant.PAGE_NUMBER_NAME, defaultValue = PageConstant.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = PageConstant.PAGE_SIZE_NAME, defaultValue = PageConstant.PAGE_SIZE, required = false) int pageSize
    ) {
        BrandListGetVm data =  brandService.getBrands(pageNo, pageSize);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getBrandById(@Min(1) @PathVariable Long id) {
        BrandGetVm data =  brandService.getBrandById(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/slug/{slug}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getBrandById(@PathVariable String slug) {
        BrandGetVm data =  brandService.getBrandBySlug(slug);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBrand(@Valid @ModelAttribute BrandPostVm brandPostVm, UriComponentsBuilder ucBuilder){
        BrandGetVm data = brandService.createBrand(brandPostVm);
        return ResponseEntity.created(
                        ucBuilder.replacePath("/brand/{id}").buildAndExpand(data.id()).toUri()
                )
                .body(new SuccessApiResponse(
                        HttpStatus.CREATED.getReasonPhrase(),
                        data
                ));
    }
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBrand(
            @Valid @ModelAttribute BrandPostVm brandPostVm,
            @Min(1) @PathVariable Long id
    ){
        BrandGetVm data = brandService.updateBrand(brandPostVm, id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBrand(@Min(1) @PathVariable Long id){
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}

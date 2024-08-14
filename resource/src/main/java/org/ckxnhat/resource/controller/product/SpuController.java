package org.ckxnhat.resource.controller.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.PageConstant;
import org.ckxnhat.resource.service.product.SpuRegularAttributeService;
import org.ckxnhat.resource.service.product.SpuSaleAttributeCombinationService;
import org.ckxnhat.resource.service.product.SpuService;
import org.ckxnhat.resource.viewmodel.product.*;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-09 20:53:49.816
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/spu")
@Validated
public class SpuController {
    private final SpuService spuService;
    private final SpuRegularAttributeService spuRegularAttributeService;
    private final SpuSaleAttributeCombinationService spuSaleAttributeCombinationService;
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getListSpuWithFilter(
            @RequestParam(name = PageConstant.PAGE_NUMBER_NAME, defaultValue = PageConstant.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = PageConstant.PAGE_SIZE_NAME, defaultValue = PageConstant.PAGE_SIZE, required = false) int pageSize,
            @Min(0) @RequestParam(name = "min_price", required = false) Double minPrice,
            @Min(0) @RequestParam(name = "max_price", required = false) Double maxPrice,
            @RequestParam(name = "brand_ids", required = false) List<@Min(0)Long> brandIds,
            @RequestParam(name = "category_ids", required = false) List<@Min(0)Long> categoryIds
    ){
        SpuListGetVm data = spuService.getListSpu(
                pageNo,
                pageSize,
                minPrice,
                maxPrice,
                brandIds,
                categoryIds
        );
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getSpuDetailById(@Min(1) @PathVariable("id") Long id){
        SpuDetailGetVm data = spuService.getSpuDetailById(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/slug/{slug}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getSpuDetailBySlug(@NotBlank @PathVariable("slug") String slug){
        SpuDetailGetVm data = spuService.getSpuDetailBySlug(slug);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping( "")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSpu(@Valid @RequestBody SpuPostVm spuPostVm, UriComponentsBuilder uriBuilder){
        SpuDetailGetVm data = spuService.createSpu(spuPostVm);
        return ResponseEntity.created(
                        uriBuilder.replacePath("/spu/{id}").buildAndExpand(data.id()).toUri()
                )
                .body(new SuccessApiResponse(
                        HttpStatus.CREATED.getReasonPhrase(),
                        data
                ));

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSpuBrief(@Min(1) @PathVariable("id") Long id, @Valid @RequestBody SpuBriefPutVm spuBriefPutVm){
        SpuDetailGetVm data = spuService.updateSpuBrief(id, spuBriefPutVm);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PutMapping("/{id}/change-thumbnail")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setSpuThumbnail(@Min(1) @PathVariable("id") Long id,@RequestPart("image") MultipartFile image){
        String data = spuService.changeSpuThumbnail(id, image);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PutMapping("/{id}/add-description-images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSpuDescriptionImages(@Min(1) @PathVariable("id") Long id,@RequestPart("images") List<MultipartFile> images){
        List<SpuDescriptionGetVm> data = spuService.addSpuDescriptionImages(id, images);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PutMapping("/{id}/add-images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSpuImages(@Min(1) @PathVariable("id") Long id,@RequestPart("images") List<MultipartFile> images){
        List<SpuImageGetVm> data = spuService.addSpuImages(id, images);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @DeleteMapping("/{id}/delete-description-images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSpuDescriptionImages(
            @Min(1) @PathVariable("id") Long id,
            @RequestParam("image_ids") List<@Min(1) Long> imageIds
    ){
        spuService.deleteSpuDescriptionImages(id, imageIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete-images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSpuImages(
            @Min(1) @PathVariable("id") Long id,
            @RequestParam("image_ids") List<@Min(1) Long> imageIds
    ){
        spuService.deleteSpuImages(id, imageIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/spu-regular-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSpuRegularAttribute(
            @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody SpuRegularAttributePostVm spuRegularAttributePostVm
    ){
        spuRegularAttributeService.addSpuRegularAttribute(id, spuRegularAttributePostVm);
        var data = spuRegularAttributeService.getSpuRegularAttributes(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PutMapping("/{id}/spu-regular-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSpuRegularAttribute(
            @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody SpuRegularAttributePostVm spuRegularAttributePostVm
    ){
        spuRegularAttributeService.updateSpuRegularAttribute(id, spuRegularAttributePostVm);
        var data = spuRegularAttributeService.getSpuRegularAttributes(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @DeleteMapping("/{id}/spu-regular-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSpuRegularAttribute(
            @Min(1) @PathVariable("id") Long id,
            @RequestParam("attribute_name_ids") List<@Min(1) Long> attributeNameIds
    ){
        spuRegularAttributeService.deleteSpuRegularAttributes(id, attributeNameIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/combination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSpuCombinations(
            @Min(1) @PathVariable("id") Long id,
            @RequestParam(name = PageConstant.PAGE_NUMBER_NAME, defaultValue = PageConstant.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = PageConstant.PAGE_SIZE_NAME, defaultValue = PageConstant.PAGE_SIZE, required = false) int pageSize
    ){
        var data = spuSaleAttributeCombinationService
                .getCombinations(id, pageNo, pageSize);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PutMapping("/{id}/generate-all-combination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generateAllCombination(
            @Min(1) @PathVariable("id") Long id
    ){
        long count = spuSaleAttributeCombinationService.generateAllSaleAttributeCombination(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                count
        ));
    }
}

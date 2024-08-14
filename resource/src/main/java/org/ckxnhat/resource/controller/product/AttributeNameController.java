package org.ckxnhat.resource.controller.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.PageConstant;
import org.ckxnhat.resource.service.product.AttributeNameService;
import org.ckxnhat.resource.viewmodel.product.AttributeNameGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeNameListGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeNamePostVm;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-09 16:36:55.337
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute-name")
@Validated
public class AttributeNameController {
    private final AttributeNameService attributeNameService;
    private final ValidationAutoConfiguration validationAutoConfiguration;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAttributeNames(
            @RequestParam(name = PageConstant.PAGE_NUMBER_NAME, defaultValue = PageConstant.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = PageConstant.PAGE_SIZE_NAME, defaultValue = PageConstant.PAGE_SIZE, required = false) int pageSize
    ) {
        AttributeNameListGetVm data = attributeNameService.getAttributeNames(pageNo, pageSize);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAttributeNameById(@Min(1) @PathVariable Long id) {
        AttributeNameGetVm data = attributeNameService.getAttributeNameById(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAttributeName(@Valid @RequestBody AttributeNamePostVm attributeNamePostVm, UriComponentsBuilder ucBuilder) {
        AttributeNameGetVm data = attributeNameService.createAttributeName(attributeNamePostVm);
        return ResponseEntity.created(
                        ucBuilder.replacePath("/attribute-name/{id}").buildAndExpand(data.id()).toUri()
                )
                .body(new SuccessApiResponse(
                        HttpStatus.CREATED.getReasonPhrase(),
                        data
                ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAttributeName(
            @Min(1) @PathVariable Long id,
            @Valid  @RequestBody AttributeNamePostVm attributeNamePostVm
    ){
        AttributeNameGetVm data = attributeNameService.updateAttributeName(id,attributeNamePostVm);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
}


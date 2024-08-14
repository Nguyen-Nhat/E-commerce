package org.ckxnhat.resource.controller.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.PageConstant;
import org.ckxnhat.resource.service.product.AttributeGroupService;
import org.ckxnhat.resource.viewmodel.product.AttributeGroupGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeGroupListGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeGroupPostVm;
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
 * @datetime 2024-08-09 14:33:58.189
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute-group")
@Validated
public class AttributeGroupController {
    private final AttributeGroupService attributeGroupService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAttributeGroups(
            @RequestParam(name = PageConstant.PAGE_NUMBER_NAME, defaultValue = PageConstant.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = PageConstant.PAGE_SIZE_NAME, defaultValue = PageConstant.PAGE_SIZE, required = false) int pageSize
    ){
        AttributeGroupListGetVm data = attributeGroupService.getAttributeGroups(pageNo, pageSize);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAttributeGroupById(@Min(1) @PathVariable Long id) {
        AttributeGroupGetVm data = attributeGroupService.getAttributeGroupById(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAttributeGroup(@Valid @RequestBody AttributeGroupPostVm attributeGroupPostVm, UriComponentsBuilder ucBuilder){
        AttributeGroupGetVm data = attributeGroupService.createAttributeGroup(attributeGroupPostVm);
        return ResponseEntity.created(
                        ucBuilder.replacePath("/attribute-group/{id}").buildAndExpand(data.id()).toUri()
                )
                .body(new SuccessApiResponse(
                        HttpStatus.CREATED.getReasonPhrase(),
                        data
                ));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAttributeGroup(
            @Min(1) @PathVariable Long id,
            @Valid @RequestBody AttributeGroupPostVm attributeGroupPostVm
    ){
        AttributeGroupGetVm data = attributeGroupService.updateAttributeGroup(id,attributeGroupPostVm);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
}

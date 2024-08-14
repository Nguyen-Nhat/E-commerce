package org.ckxnhat.resource.controller.search;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.service.search.SpuSearchService;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.ckxnhat.resource.viewmodel.search.SpuListGetVm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-31 14:38:37.309
 */

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class SearchController {
    private final SpuSearchService spuSearchService;
    @GetMapping("/spu/suggest")
    public ResponseEntity<?> spuSearchAutoComplete(@RequestParam String q, @RequestParam int size){
        List<String> data = spuSearchService.autoCompleteSearch(q, size);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/spu")
    public ResponseEntity<?> spuSearch(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "default") String sort
    ){
        SpuListGetVm data = spuSearchService.spuSearch(q, page, size, brandId, categoryId, minPrice, maxPrice, sort);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
}

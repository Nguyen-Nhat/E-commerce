package org.ckxnhat.resource.service.search;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.document.Spu;
import org.ckxnhat.resource.document.field.SpuField;
import org.ckxnhat.resource.repository.search.SpuSearchRepository;
import org.ckxnhat.resource.service.other.S3Service;
import org.ckxnhat.resource.viewmodel.search.SpuGetVm;
import org.ckxnhat.resource.viewmodel.search.SpuListGetVm;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-30 20:20:46.314
 */

@Service
@RequiredArgsConstructor
public class SpuSearchService {
    private final SpuSearchRepository spuSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final S3Service s3Service;
    public List<String> autoCompleteSearch(String keyword, int size){
        Pageable pageable = PageRequest.of(0, size);
        return spuSearchRepository.autoCompleteSearch(keyword, pageable)
                .stream()
                .map(Spu::getName)
                .toList();
    }

    public SpuListGetVm spuSearch(String keyword, Integer page, Integer size, Long brandId, Long categoryId, Double minPrice, Double maxPrice, String sort){
        // query
        Query.Builder keyQueryBuilder = new Query.Builder();
        Query.Builder isDeletedQueryBuilder = new Query.Builder();
        keyQueryBuilder.match(m -> m.field(SpuField.NAME).query(keyword).fuzziness(Fuzziness.AUTO.asString()).operator(Operator.And));
        isDeletedQueryBuilder.term(t -> t.field(SpuField.IS_DELETED).value(false));

        //sort
        Sort s;
        if(sort.equals("asc")){
            s = Sort.by(Sort.Direction.ASC, SpuField.PRICE);
        }
        else if(sort.equals("desc")){
            s = Sort.by(Sort.Direction.DESC, SpuField.PRICE);
        }
        else {
            // priority
            s = Sort.by(Sort.Direction.DESC, SpuField.SORT);
        }

        // pagination
        Pageable pageable = PageRequest.of(page, size);
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b2 -> b2.must(keyQueryBuilder.build(), isDeletedQueryBuilder.build()))
                )
                .withFilter(f -> f
                        .bool(b -> {
                            handleTerm(brandId, SpuField.BRAND_ID, b);
                            handleTerm(categoryId, SpuField.CATEGORY_ID, b);
                            handleRange(minPrice, maxPrice, SpuField.PRICE, b);
                            return b;
                        })
                )
                .withPageable(pageable)
                .withSort(s)
                .build();
        SearchHits<Spu> result = elasticsearchOperations.search(nativeQuery, Spu.class);
        SearchPage<Spu> resultWithPage = SearchHitSupport.searchPageFor(result, nativeQuery.getPageable());
        List<SpuGetVm> data = result.stream()
                .map(SearchHit::getContent)
                .map(spu -> {
                    String signedImageUrl = s3Service.getSignedUrl(spu.getThumbnailId());
                    spu.setThumbnailId(signedImageUrl);
                    return SpuGetVm.fromModel(spu);
                })
                .toList();
        return new SpuListGetVm(
                data,
                resultWithPage.getNumber(),
                resultWithPage.getSize(),
                resultWithPage.getNumberOfElements(),
                resultWithPage.getTotalPages(),
                resultWithPage.isLast()
        );
    }
    // filter brand and category
    private void handleTerm(Long value, String field, BoolQuery.Builder b){
        if(value != null){
            b.must(m -> m
                    .term(t -> t
                            .field(field)
                            .value(value)
                    )
            );
        }
    }
    // filter range price
    private void handleRange(Double min, Double max, String field, BoolQuery.Builder b){
        if(min != null || max != null){
            b.must(m -> m
                    .range(r -> r
                            .field(field)
                            .from(min != null ? min.toString() : null)
                            .to(max != null ? max.toString() : null)
                    )
            );
        }
    }
}

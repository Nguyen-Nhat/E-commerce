package org.ckxnhat.resource.repository.search;

import org.ckxnhat.resource.document.Spu;
import org.ckxnhat.resource.document.field.SpuField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-30 20:12:09.118
 */

@Repository
public interface SpuSearchRepository extends ElasticsearchRepository<Spu, Long> {
    @Query("""
            "bool": {
                "must": [
                    {
                        "match": {
                            #{@spuField.NAME}": {
                                "query": "#{#keyword}",
                                "operator": "and",
                                "fuzziness": "AUTO"
                            }
                        }
                    }.
                    {
                        "term": {
                            "#{@spuField.IS_DELETED}": false
                        }
                    }
                ]
            }
            """)
    @SourceFilters(includes = {SpuField.NAME})
    Page<Spu> autoCompleteSearch(String keyword, Pageable pageable);
}

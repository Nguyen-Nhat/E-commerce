package org.ckxnhat.resource.document;

import jakarta.persistence.Id;
import lombok.*;
import org.ckxnhat.resource.document.field.SpuField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-30 10:44:42.464
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product")
@Setting(settingPath = "esconfig/spu-analyzer.json")
public class Spu {
    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "index", searchAnalyzer = "search", name = SpuField.NAME)
    private String name;

    @Field(name = SpuField.THUMBNAIL_ID)
    private String thumbnailId;

    @Field(name = SpuField.SLUG)
    private String slug;

    @Field(type = FieldType.Double, name = SpuField.PRICE)
    private Double price;

    @Field(type = FieldType.Short, name = SpuField.SORT)
    private Short sort; // priority

    @Field(type = FieldType.Long, name = SpuField.BRAND_ID)
    private Long brandId;

    @Field(type = FieldType.Long, name = SpuField.CATEGORY_ID)
    private Long categoryId;

    @Field(name = SpuField.IS_PUBLISHED)
    private boolean isPublished;

    @Field(name = SpuField.IS_DELETED)
    private boolean isDeleted;
}

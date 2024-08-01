package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 08:15:34.824
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attribute_name")
public class AttributeName {
    @Id
    private Long id;
    private String name;
    private short type;
    private boolean isRequired;
    private boolean hasOptions;

    @OneToMany(mappedBy = "attributeName")
    private List<AttributeNameGroup> attributeNameGroups = new ArrayList<>();

    @OneToMany(mappedBy = "attributeName", fetch = FetchType.LAZY)
    private List<SkuSaleAttributeValue> skuSaleAttributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "attributeName", fetch = FetchType.LAZY)
    private List<SpuRegularAttributeValue> spuRegularAttributeValues = new ArrayList<>();
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof AttributeName)) return false;
        return id != null && id.equals(((AttributeName) obj).id);
    }
}

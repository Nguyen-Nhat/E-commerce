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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "attribute_group_id")
    private AttributeGroup attributeGroup;

    @OneToMany(mappedBy = "attributeName", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuSaleAttributeMapping> spuSaleAttributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "attributeName", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuRegularAttributeValue> spuRegularAttributeValues = new ArrayList<>();
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof AttributeName)) return false;
        return id != null && id.equals(((AttributeName) obj).id);
    }

    /*
     * one bucket Set/ Hashmap
     * https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     **/
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

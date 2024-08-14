package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 18:41:45.595
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spu_sale_attribute_value")
public class SpuSaleAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "spu_sale_attribute_mapping_id")
    private SpuSaleAttributeMapping spuSaleAttributeMapping;
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SpuSaleAttributeValue)) return false;
        return id != null && id.equals(((SpuSaleAttributeValue)obj).id);
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

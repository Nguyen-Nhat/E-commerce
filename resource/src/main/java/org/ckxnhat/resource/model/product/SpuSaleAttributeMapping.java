package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 18:41:41.532
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spu_sale_attribute_mapping")
public class SpuSaleAttributeMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "spu_id")
    private Spu spu;

    @ManyToOne
    @JoinColumn(name = "attribute_name_id")
    private AttributeName attributeName;

    @OneToMany(mappedBy = "spuSaleAttributeMapping", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SpuSaleAttributeValue> spuSaleAttributeValues = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SpuSaleAttributeMapping)) return false;
        return id != null && id.equals(((SpuSaleAttributeMapping)obj).id);
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
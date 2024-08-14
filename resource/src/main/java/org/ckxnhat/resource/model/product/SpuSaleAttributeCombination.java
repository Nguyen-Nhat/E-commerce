package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 18:41:48.850
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spu_sale_attribute_combination")
public class SpuSaleAttributeCombination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spu_id")
    private Spu spu;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sku_id")
    private Sku sku;


    private String attributeKey;

    @Transient
    @Builder.Default
    private HashMap<String, String> attribute = new HashMap<>();

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SpuSaleAttributeCombination)) return false;
        return id != null && id.equals(((SpuSaleAttributeCombination)obj).id);
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

package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.ckxnhat.resource.model.AbstractAuditEntity;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 11:17:55.126
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sku")
public class Sku extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String gtin;
    private double price;
    private boolean isAllowedToOrder;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "spu_id")
    private Spu spu;

    @OneToMany(mappedBy = "sku")
    private List<SkuImage> skuImages;

    @OneToMany(mappedBy = "sku")
    private List<SkuSaleAttributeValue> skuSaleAttributeValues;
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Sku)) return false;
        return id != null && id.equals(((Sku)obj).id);
    }
}

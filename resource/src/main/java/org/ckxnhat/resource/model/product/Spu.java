package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.ckxnhat.resource.model.AbstractAuditEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 10:52:20.827
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spu")
public class Spu extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String thumbnailId;
    private String slug;
    private double minPrice;
    private short sort;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "spu", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuDescription> descriptions = new ArrayList<>();

    @OneToMany(mappedBy = "spu", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuRelated> relatedSpus = new ArrayList<>();

    @OneToMany(mappedBy = "spu", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "spu", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuRegularAttributeValue> regularAttributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "spu", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuSaleAttributeMapping> spuSaleAttributeMappings = new ArrayList<>();

    @OneToMany(mappedBy = "spu", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpuSaleAttributeCombination> spuSaleAttributeCombinations = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Spu)) return false;
        return id != null && id.equals(((Spu) obj).id);
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

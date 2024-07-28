package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.ckxnhat.resource.model.AbstractAuditEntity;

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
    private short sort;
    private boolean isPublished;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "spu")
    private List<Sku> skus;
    @OneToMany(mappedBy = "spu")
    private List<SpuDescription> descriptions;
    @OneToMany(mappedBy = "spu")
    private List<SpuRegularAttributeValue> regularAttributeValues;

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Spu)) return false;
        return id != null && id.equals(((Spu) obj).id);
    }
}

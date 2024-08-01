package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 23:20:29.848
 */

@Getter
@Setter
@Entity
@Table(name = "category_brand_relation")
public class CategoryBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof CategoryBrand)) return false;
        return id != null && id.equals(((CategoryBrand)obj).id);
    }
}

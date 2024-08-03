package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.ckxnhat.resource.model.AbstractAuditEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 22:37:56.306
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String slug;
    private String imageId;
    private int categoryLevel;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<CategoryBrand> categoryBrands = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<AttributeGroup> attributeGroup;
    @OneToMany(mappedBy = "category")
    private List<Spu> spus = new ArrayList<>();
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Category)) return false;
        return id != null && id.equals(((Category)obj).id);
    }
}

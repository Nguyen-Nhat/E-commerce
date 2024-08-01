package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;
import org.ckxnhat.resource.model.AbstractAuditEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 21:57:36.462
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "brand")
public class Brand extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;

    private String description;

    private String imageId;

    private boolean isPublished;

    private boolean isDeleted;

    @OneToMany(mappedBy = "brand")
    private List<CategoryBrand> categories = new ArrayList<>();
    @OneToMany(mappedBy = "brand")
    private List<Spu> spus = new ArrayList<>();
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Brand)) return false;
        return id != null && id.equals(((Brand)obj).id);
    }
}

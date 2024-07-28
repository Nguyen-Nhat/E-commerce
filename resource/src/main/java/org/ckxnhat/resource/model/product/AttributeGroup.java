package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 07:51:35.500
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attribute_group")
public class AttributeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "attributeGroup")
    private List<AttributeNameGroup> attributeNameGroups = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof AttributeGroup)) return false;
        return id != null && id.equals(((AttributeGroup)obj).id);
    }
}

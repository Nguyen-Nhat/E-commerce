package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 10:35:54.244
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attribute_name_group_relation")
public class AttributeNameGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "attribute_name_id")
    private AttributeName attributeName;
    @ManyToOne
    @JoinColumn(name = "attribute_group_id")
    private AttributeGroup attributeGroup;

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof AttributeNameGroup)) return false;
        return id != null && id.equals(((AttributeNameGroup)obj).id);
    }
}

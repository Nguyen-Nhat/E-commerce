package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 14:56:13.646
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "spu_regular_attribute_value")
public class SpuRegularAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "spu_id")
    private Spu spu;

    @ManyToOne
    @JoinColumn(name = "attribute_name_id")
    private AttributeName attributeName;

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SpuRegularAttributeValue)) return false;
        return id != null && id.equals(((SpuRegularAttributeValue)obj).getId());
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

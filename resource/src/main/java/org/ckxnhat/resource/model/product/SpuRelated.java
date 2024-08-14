package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 22:39:18.791
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spu_related")
public class SpuRelated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spu_id")
    private Spu spu;

    @ManyToOne
    @JoinColumn(name = "related_spu_id")
    private Spu relatedSpu;

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SpuRelated)) return false;
        return id != null && id.equals(((SpuRelated)obj).id);
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

package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 13:30:16.416
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "spu_description")
public class SpuDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spu_id")
    private Spu spu;

    private String imageId;

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SpuDescription)) return false;
        return id != null && id.equals(((SpuDescription)obj).id);
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

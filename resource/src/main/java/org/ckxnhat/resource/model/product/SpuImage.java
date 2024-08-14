package org.ckxnhat.resource.model.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-11 09:08:25.626
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spu_image")
public class SpuImage {
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
        if(!(obj instanceof SpuImage)) return false;
        return id != null && id.equals(((SpuImage)obj).getId());
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

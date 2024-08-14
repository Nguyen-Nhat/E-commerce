package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuRelated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-06 12:27:38.878
 */

@Repository
public interface SpuRelatedRepository extends JpaRepository<SpuRelated, Long> {
}

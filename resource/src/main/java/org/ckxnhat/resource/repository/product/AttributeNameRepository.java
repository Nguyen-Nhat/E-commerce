package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.AttributeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:02:53.827
 */

@Repository
public interface AttributeNameRepository extends JpaRepository<AttributeName, Long> {
}

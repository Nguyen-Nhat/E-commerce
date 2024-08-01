package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.AttributeNameGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:03:28.254
 */

@Repository
public interface AttributeNameGroupRepository extends JpaRepository<AttributeNameGroup, Long> {
}

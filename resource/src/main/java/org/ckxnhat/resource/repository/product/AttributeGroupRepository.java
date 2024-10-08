package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.AttributeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:02:03.948
 */

@Repository
public interface AttributeGroupRepository extends JpaRepository<AttributeGroup, Long> {
    Page<AttributeGroup> findAll(Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}

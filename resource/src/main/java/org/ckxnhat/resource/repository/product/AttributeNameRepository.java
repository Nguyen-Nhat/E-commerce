package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.AttributeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:02:53.827
 */

@Repository
public interface AttributeNameRepository extends JpaRepository<AttributeName, Long> {
    @Query("SELECT an FROM AttributeName an " +
            "WHERE an.attributeGroup.category.id = :categoryId " +
            "AND an.id IN :ids"
    )
    List<AttributeName> findAllByIdsAndCategoryId(Collection<Long> ids, Long categoryId);
}

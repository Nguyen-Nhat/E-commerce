package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.CategoryBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:06:30.458
 */

@Repository
public interface CategoryBrandRepository extends JpaRepository<CategoryBrand, Long> {
}

package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:04:23.874
 */

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}

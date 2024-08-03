package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:04:23.874
 */

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b WHERE b.isDeleted = false")
    Page<Brand> findAllNotDeleted(Pageable pageable);
    Optional<Brand> findByName(String name);
    Optional<Brand> findBySlug(String slug);
    boolean existsBySlug(String slug);
}

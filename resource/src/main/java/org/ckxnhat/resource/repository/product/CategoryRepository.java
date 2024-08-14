package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:04:58.407
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.isDeleted = FALSE")
    Page<Category> findAllNotDeleted(Pageable pageable);
    Optional<Category> findBySlug(String slug);
    boolean existsById(Long id);
    boolean existsBySlug(String slug);
}

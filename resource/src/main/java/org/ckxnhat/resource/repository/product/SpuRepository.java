package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:12:12.504
 */

@Repository
public interface SpuRepository extends JpaRepository<Spu, Long> {
    @Query("SELECT s FROM Spu s WHERE " +
            "(:minPrice IS NULL OR s.minPrice <= :minPrice) AND" +
            "(:maxPrice IS NULL OR s.minPrice <= :maxPrice) AND" +
            "(COALESCE(:brands, NULL) IS NULL OR s.brand.id IN :brands) AND" +
            "(COALESCE(:categories, NULL) IS NULL OR s.category.id IN :categories) AND" +
            "(s.isDeleted = FALSE)" +
            "ORDER BY s.sort DESC"
    )
    Page<Spu> findAllWithFilter(
            Double minPrice,
            Double maxPrice,
            List<Long> brands,
            List<Long> categories,
            Pageable pageable
    );
    Optional<Spu> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
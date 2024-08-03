package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.config.S3Config;
import org.ckxnhat.resource.exception.BadRequestException;
import org.ckxnhat.resource.exception.DuplicatedException;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.Brand;
import org.ckxnhat.resource.repository.product.BrandRepository;
import org.ckxnhat.resource.service.other.S3Service;
import org.ckxnhat.resource.viewmodel.product.BrandGetVm;
import org.ckxnhat.resource.viewmodel.product.BrandListGetVm;
import org.ckxnhat.resource.viewmodel.product.BrandPostVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-01 13:27:18.206
 */

@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final S3Service s3Service;
    public BrandListGetVm getBrands(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Brand> brands = brandRepository.findAll(pageable);
        List<BrandGetVm> brandGetVms = brands
                .stream()
                .map(this::normalizeData)
                .toList();
        return new BrandListGetVm(
                brandGetVms,
                brands.getNumber(),
                brands.getSize(),
                brands.getTotalElements(),
                brands.getTotalPages(),
                brands.isLast()
        );
    }
    public BrandGetVm getBrandById(Long id){
        Brand brand = brandRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("KHông tim thấy"));
        if(brand.isDeleted()){
            throw new BadRequestException("brand co id nay da bi xoa");
        }
        return normalizeData(brand);
    }
    public BrandGetVm getBrandBySlug(String slug){
        Brand brand = brandRepository
                .findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("KHông tim thấy"));
        if(brand.isDeleted()){
            throw new BadRequestException("brand co id nay da bi xoa");
        }
        return normalizeData(brand);
    }
    public BrandGetVm createBrand(BrandPostVm brandPostVm){
        if(brandRepository.existsBySlug(brandPostVm.slug())){
            throw new DuplicatedException("tồn tại rồi");
        }
        String imageId = s3Service.uploadImage(brandPostVm.image());
        Brand brand = Brand.builder()
                .name(brandPostVm.name())
                .slug(brandPostVm.slug())
                .imageId(imageId)
                .description(brandPostVm.description())
                .build();
        Brand result = brandRepository.save(brand);
        return normalizeData(result);
    }
    public BrandGetVm updateBrand(BrandPostVm brandPostVm, Long id){
        Brand brand = brandRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(""));
        if(brand.isDeleted()){
            throw new BadRequestException("brand co id nay da bi xoa");
        }
        if(!brandPostVm.slug().equals(brand.getSlug()) && brandRepository.existsBySlug(brandPostVm.slug())){
            throw new DuplicatedException("tồn tại rồi");
        }
        if(brand.getImageId() != null){
            s3Service.deleteImage(brand.getImageId());
        }
        String imageId = s3Service.uploadImage(brandPostVm.image());
        brand.setName(brandPostVm.name());
        brand.setSlug(brandPostVm.slug());

        brand.setImageId(imageId);
        brand.setDescription(brandPostVm.description());
        brandRepository.save(brand);
        return normalizeData(brand);
    }

    public void deleteBrandById(Long id){
        Brand brand = brandRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(""));
        brand.setDeleted(true);
        brandRepository.save(brand);
    }
    private BrandGetVm normalizeData(Brand brand){
        String imageUrl = s3Service.getSignedUrl(brand.getImageId());
        return new BrandGetVm(
                brand.getId(),
                brand.getName(),
                brand.getSlug(),
                brand.getDescription(),
                imageUrl
        );
    }
}

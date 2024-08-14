package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
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
                .map(item -> BrandGetVm.fromModel(item, s3Service))
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
        Brand brand = findBrandById(id);
        checkBrandIsDeleted(brand);
        return BrandGetVm.fromModel(brand, s3Service);
    }
    public BrandGetVm getBrandBySlug(String slug){
        Brand brand = findBrandBySlug(slug);
        checkBrandIsDeleted(brand);
        return BrandGetVm.fromModel(brand, s3Service);
    }

    public BrandGetVm createBrand(BrandPostVm brandPostVm){
        if(brandRepository.existsBySlug(brandPostVm.slug())){
            throw new DuplicatedException(ErrorCodeConstant.BRAND_SLUG_IS_DUPLICATED, brandPostVm.slug());
        }
        String imageId = s3Service.uploadImage(brandPostVm.image());
        Brand brand = Brand.builder()
                .name(brandPostVm.name())
                .slug(brandPostVm.slug())
                .imageId(imageId)
                .description(brandPostVm.description())
                .build();
        brandRepository.save(brand);
        return BrandGetVm.fromModel(brand, s3Service);
    }
    public BrandGetVm updateBrand(BrandPostVm brandPostVm, Long id){
        Brand brand = findBrandById(id);
        if(!brandPostVm.slug().equals(brand.getSlug()) && brandRepository.existsBySlug(brandPostVm.slug())){
            throw new DuplicatedException(ErrorCodeConstant.BRAND_SLUG_IS_DUPLICATED, brandPostVm.slug());
        }
        s3Service.deleteImage(brand.getImageId());
        String imageId = s3Service.uploadImage(brandPostVm.image());
        brand.setName(brandPostVm.name());
        brand.setSlug(brandPostVm.slug());

        brand.setImageId(imageId);
        brand.setDescription(brandPostVm.description());
        brandRepository.save(brand);
        return BrandGetVm.fromModel(brand, s3Service);
    }

    public void deleteBrandById(Long id){
        Brand brand = findBrandById(id);
        brand.setDeleted(true);
        brandRepository.save(brand);
    }



    private Brand findBrandById(Long id){
        return brandRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.BRAND_ID_NOT_FOUND, id));
    }
    private Brand findBrandBySlug(String slug){
        return brandRepository
                .findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.BRAND_SLUG_NOT_FOUND, slug));
    }
    private void checkBrandIsDeleted(Brand brand){
        if(brand.isDeleted()){
            throw new BadRequestException(ErrorCodeConstant.BRAND_IS_DELETED, brand.getId());
        }
    }
}

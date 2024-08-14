package org.ckxnhat.resource.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.BadRequestException;
import org.ckxnhat.resource.exception.DuplicatedException;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.*;
import org.ckxnhat.resource.repository.product.*;
import org.ckxnhat.resource.service.other.S3Service;
import org.ckxnhat.resource.viewmodel.product.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 21:29:14.400
 */

@Service
@Transactional
@RequiredArgsConstructor
public class SpuService {
    private final EntityManager entityManager;
    private final SpuRepository spuRepository;
    private final SpuImageRepository spuImageRepository;
    private final SpuRelatedRepository spuRelatedRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SpuDescriptionRepository spuDescriptionRepository;
    private final SpuSaleAttributeValueRepository spuSaleAttributeValueRepository;
    private final AttributeNameRepository attributeNameRepository;
    private final S3Service s3Service;
    private final SpuRegularAttributeValueRepository spuRegularAttributeValueRepository;
    private final SpuSaleAttributeMappingRepository spuSaleAttributeMappingRepository;

    public SpuListGetVm getListSpu(int pageNo, int pageSize, Double minPrice, Double maxPrice, List<Long> brandIds, List<Long> categoryIds) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Spu> spuPage = spuRepository.findAllWithFilter(minPrice, maxPrice, brandIds, categoryIds, pageable);
        List<SpuGetVm> data = spuPage.getContent()
                .stream().
                map(item -> SpuGetVm.fromModel(item, s3Service))
                .toList();
        return new SpuListGetVm(
                data,
                spuPage.getNumber(),
                spuPage.getSize(),
                spuPage.getTotalElements(),
                spuPage.getTotalPages(),
                spuPage.isLast()
        );
    }

    public SpuDetailGetVm getSpuDetailById(Long id) {
        Spu spu = findSpuById(id);
        checkSpuIsDeleted(spu);
        setUpSaleAttribute(spu);
        return SpuDetailGetVm.fromModel(spu, s3Service);
    }

    public SpuDetailGetVm getSpuDetailBySlug(String slug) {
        Spu spu = findSpuBySlug(slug);
        checkSpuIsDeleted(spu);
        setUpSaleAttribute(spu);
        return SpuDetailGetVm.fromModel(spu, s3Service);
    }

    public SpuDetailGetVm createSpu(SpuPostVm spuPostVm) {
        if (spuRepository.existsBySlug(spuPostVm.slug())) {
            throw new DuplicatedException(ErrorCodeConstant.SPU_SLUG_IS_DUPLICATED, spuPostVm.slug());
        }
        Brand brand = findBrandById(spuPostVm.brandId());
        Category category = findCategoryById(spuPostVm.categoryId());

        Spu spu = Spu.builder()
                .name(spuPostVm.name())
                .slug(spuPostVm.slug())
                .brand(brand)
                .category(category)
                .sort(spuPostVm.sort())
                .minPrice(spuPostVm.minPrice())
                .build();
        Spu saveSpu = spuRepository.saveAndFlush(spu);

        HashSet<Long> relatedSpuIds = new HashSet<>(spuPostVm
                .relatedSpuIds());
        if (relatedSpuIds.size() < spuPostVm.relatedSpuIds().size()) {
            throw new BadRequestException(ErrorCodeConstant.LIST_RELATED_SPU_INPUT_IS_DUPLICATED);
        }
        List<Spu> relatedSpus = spuRepository.findAllById(relatedSpuIds);
        if (relatedSpus.size() != spuPostVm.relatedSpuIds().size()) {
            HashSet<Long> foundIds = relatedSpus
                    .stream()
                    .map(Spu::getId)
                    .collect(Collectors.toCollection(HashSet::new));
            List<Long> notFoundIds = spuPostVm
                    .relatedSpuIds()
                    .stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new NotFoundException(ErrorCodeConstant.LIST_SPU_IS_NOT_FOUND, notFoundIds.toString());
        }

        spuRelatedRepository.saveAllAndFlush(relatedSpus.stream()
                .map(item -> SpuRelated.builder()
                        .spu(saveSpu)
                        .relatedSpu(item)
                        .build()
                )
                .toList()
        );


        List<Long> saleAttrNameIds = spuPostVm
                .saleAttributes()
                .stream()
                .map(SpuSaleAttributePostVm::nameAttributeId)
                .toList();
        /*
         * check duplication in list sale attribute name id from user
         * */
        if (saleAttrNameIds.size() < spuPostVm.saleAttributes().size()) {
            throw new DuplicatedException(ErrorCodeConstant.LIST_SALE_ATTR_INPUT_IS_DUPLICATED);
        }

        List<Long> regularAttrNameIds = spuPostVm
                .regularAttributes()
                .stream()
                .map(SpuRegularAttributePostVm::nameAttributeId)
                .toList();

        /*
         * check duplication in list regular attribute name id from user
         * */
        if (regularAttrNameIds.size() < spuPostVm.regularAttributes().size()) {
            throw new DuplicatedException(ErrorCodeConstant.LIST_REGULAR_ATTR_INPUT_IS_DUPLICATED);
        }

        /*
         * check all attr in regularAttr and saleAttr to make sure
         * all required attributes are present
         * */
        List<Long> duplicatedAttributeNameIds = new ArrayList<>(saleAttrNameIds);
        duplicatedAttributeNameIds.retainAll(regularAttrNameIds);
        if (!duplicatedAttributeNameIds.isEmpty()) {
            throw new DuplicatedException(
                    ErrorCodeConstant.SALE_ATTR_ID_AND_REGULAR_ATTR_ID_DUPLICATED,
                    duplicatedAttributeNameIds.toString()
            );
        }

        List<AttributeName> saleAttrName = this.getAttributeNameFromIdAndCategoryId(
                saleAttrNameIds, category.getId(),
               ErrorCodeConstant.SALE_ATTR_ID_NOT_IN_CATEGORY_ID
        );
        List<AttributeName> regularAttrName = this.getAttributeNameFromIdAndCategoryId(
                regularAttrNameIds, category.getId(),
                ErrorCodeConstant.REGULAR_ATTR_ID_NOT_IN_CATEGORY_ID
        );

        /*
         * mapping sale attribute value with id
         **/
        HashMap<Long, List<String>> saleAttributeValueMap = spuPostVm
                .saleAttributes()
                .stream()
                .collect(Collectors.toMap(
                        SpuSaleAttributePostVm::nameAttributeId,
                        SpuSaleAttributePostVm::valueAttributes,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));

        /*
         * map regular attribute value with id
         **/
        HashMap<Long, String> regularAttributeValueMap = spuPostVm
                .regularAttributes()
                .stream()
                .collect(Collectors.toMap(
                        SpuRegularAttributePostVm::nameAttributeId,
                        SpuRegularAttributePostVm::valueAttribute,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));

        /*
         * save sale attribute name and value
         **/
        List<SpuSaleAttributeMapping> saleAttributeMappings = saleAttrName
                .stream()
                .map(item -> {
                    List<SpuSaleAttributeValue> saleAttributeValues = saleAttributeValueMap.get(item.getId())
                            .stream()
                            .map(str -> SpuSaleAttributeValue.builder()
                                    .value(str)
                                    .build()
                            )
                            .toList();


                    SpuSaleAttributeMapping mapping = SpuSaleAttributeMapping
                            .builder()
                            .spu(saveSpu)
                            .spuSaleAttributeValues(saleAttributeValues)
                            .attributeName(item)
                            .build();
                    saleAttributeValues.forEach(value -> value.setSpuSaleAttributeMapping(mapping));

                    return mapping;
                })
                .toList();
        spuSaleAttributeMappingRepository.saveAllAndFlush(saleAttributeMappings);

        /*
         * save regular attribute name and value
         **/
        List<SpuRegularAttributeValue> regularAttributeValues = regularAttrName
                .stream()
                .map(item -> SpuRegularAttributeValue
                        .builder()
                        .spu(saveSpu)
                        .value(regularAttributeValueMap.get(item.getId()))
                        .attributeName(item)
                        .build())
                .toList();
        spuRegularAttributeValueRepository.saveAllAndFlush(regularAttributeValues);

        entityManager.refresh(saveSpu);
        return SpuDetailGetVm.fromModel(saveSpu, s3Service);
    }

    public SpuDetailGetVm updateSpuBrief(Long id, SpuBriefPutVm spuBriefPutVm) {
        Spu spu = findSpuById(id);
        if (!spu.getSlug().equals(spuBriefPutVm.slug()) &&
                spuRepository.existsBySlug(spuBriefPutVm.slug())) {
            throw new DuplicatedException(ErrorCodeConstant.SPU_SLUG_IS_DUPLICATED, spuBriefPutVm.slug());
        }
        spu.setName(spuBriefPutVm.name());
        spu.setSlug(spuBriefPutVm.slug());
        spu.setMinPrice(spuBriefPutVm.minPrice());
        spu.setSort(spuBriefPutVm.sort());
        if (spuBriefPutVm.brandId() != null) {
            Brand brand = findBrandById(spuBriefPutVm.brandId());
            spu.setBrand(brand);
        }
        if (spuBriefPutVm.categoryId() != null) {
            Category category = findCategoryById(spuBriefPutVm.categoryId());
            spu.setCategory(category);
        }
        spuRepository.save(spu);
        return SpuDetailGetVm.fromModel(spu, s3Service);
    }

    public String changeSpuThumbnail(Long id, MultipartFile image) {
        Spu spu = findSpuById(id);
        s3Service.deleteImage(spu.getThumbnailId());
        String objectKey = s3Service.uploadImage(image);
        spu.setThumbnailId(objectKey);
        spuRepository.save(spu);
        return s3Service.getSignedUrl(objectKey);
    }


    public List<SpuImageGetVm> addSpuImages(Long id, List<MultipartFile> images) {
        Spu spu = findSpuById(id);
        List<SpuImage> data = s3Service.uploadImages(images)
                .stream()
                .map(item -> SpuImage.builder()
                        .spu(spu)
                        .imageId(item)
                        .build()
                )
                .toList();
        spuImageRepository.saveAll(data);
        return spuImageRepository.findAllBySpuId(id)
                .stream()
                .map(item -> SpuImageGetVm.fromModel(item, s3Service))
                .toList();
    }

    public List<SpuDescriptionGetVm> addSpuDescriptionImages(Long id, List<MultipartFile> images) {
        Spu spu = findSpuById(id);
        List<SpuDescription> data = s3Service.uploadImages(images)
                .stream()
                .map(item -> SpuDescription.builder()
                        .spu(spu)
                        .imageId(item)
                        .build()
                )
                .toList();
        spuDescriptionRepository.saveAll(data);
        return spuDescriptionRepository.findAllBySpuId(id)
                .stream()
                .map(item -> SpuDescriptionGetVm.fromModel(item, s3Service))
                .toList();
    }

    public void deleteSpuImages(Long id, List<Long> spuImageIds) {
        Spu spu = findSpuById(id);
        String[] imageKeys = spuImageRepository.findAllBySpuIdAndIdIn(id, spuImageIds)
                .stream()
                .map(SpuImage::getImageId)
                .toArray(String[]::new);
        s3Service.deleteImages(imageKeys);
        spuImageRepository.deleteBySpuIdAndIdIn(id, spuImageIds);
    }

    public void deleteSpuDescriptionImages(Long id, List<Long> imageIds) {
        Spu spu = findSpuById(id);
        String[] imageKeys = spuDescriptionRepository.findAllBySpuIdAndIdIn(id, imageIds)
                .stream()
                .map(SpuDescription::getImageId)
                .toArray(String[]::new);
        s3Service.deleteImages(imageKeys);
        spuDescriptionRepository.deleteBySpuIdAndIdIn(id, imageIds);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.CATEGORY_ID_NOT_FOUND, id));
    }

    private Brand findBrandById(Long id) {
        return brandRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.BRAND_ID_NOT_FOUND, id));
    }

    private Spu findSpuById(Long id) {
        return spuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SPU_ID_NOT_FOUND, id));
    }

    private Spu findSpuBySlug(String slug) {
        return spuRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SPU_SLUG_NOT_FOUND, slug));
    }

    private void checkSpuIsDeleted(Spu spu) {
        if (spu.isDeleted()) {
            throw new BadRequestException(ErrorCodeConstant.SPU_IS_DELETED, spu.getId());
        }
    }

    private List<AttributeName> getAttributeNameFromIdAndCategoryId(Collection<Long> ids, Long categoryId, String errorCode) {
        List<AttributeName> attrName = attributeNameRepository
                .findAllByIdsAndCategoryId(ids, categoryId);
        if (attrName.size() < ids.size()) {
            HashSet<Long> saleAttrNameIdsFromDB = attrName
                    .stream()
                    .map(AttributeName::getId)
                    .collect(Collectors.toCollection(HashSet::new));
            List<Long> missingIds = ids.stream()
                    .filter(id -> !saleAttrNameIdsFromDB.contains(id))
                    .toList();
            throw new NotFoundException(
                    errorCode,
                    missingIds.toString(),
                    categoryId
            );
        }
        return attrName;
    }

    private void setUpSaleAttribute(Spu spu) {
        HashMap<Long, SpuSaleAttributeValue> map = new HashMap<>();
        for (var item : spu.getSpuSaleAttributeMappings()) {
            for (var i : item.getSpuSaleAttributeValues()) {
                map.put(i.getId(), i);
            }
        }
        Gson gson = new Gson();
        Type longListType = new TypeToken<List<Long>>() {
        }.getType();
        for (var item : spu.getSpuSaleAttributeCombinations()) {
            List<Long> valueIds = gson.fromJson(item.getAttributeKey(), longListType);
            for(Long valueId : valueIds){
                SpuSaleAttributeValue spuSaleAttributeValue = map.get(valueId);
                item.getAttribute().put(
                        spuSaleAttributeValue.getSpuSaleAttributeMapping().getAttributeName().getName(),
                        spuSaleAttributeValue.getValue()
                );
            }
        }
    }
}

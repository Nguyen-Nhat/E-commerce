package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.BadRequestException;
import org.ckxnhat.resource.exception.DuplicatedException;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.Sku;
import org.ckxnhat.resource.model.product.SpuSaleAttributeCombination;
import org.ckxnhat.resource.repository.product.SkuRepository;
import org.ckxnhat.resource.repository.product.SpuRepository;
import org.ckxnhat.resource.repository.product.SpuSaleAttributeCombinationRepository;
import org.ckxnhat.resource.viewmodel.product.SkuInfoGetVm;
import org.ckxnhat.resource.viewmodel.product.SkuPostVm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-13 18:41:05.819
 */

@Service
@Transactional
@RequiredArgsConstructor
public class SkuService {
    private final SkuRepository skuRepository;
    private final SpuSaleAttributeCombinationRepository spuSaleAttributeCombinationRepository;

    public SkuInfoGetVm getSkuById(Long id){
        Sku sku = findSkuById(id);
        return SkuInfoGetVm.fromModel(sku);
    }
    public SkuInfoGetVm getSkuByGtin(String gtin){
        Sku sku = findSkuByGtin(gtin);
        return SkuInfoGetVm.fromModel(sku);
    }

    public SkuInfoGetVm create(SkuPostVm skuPostVm){
        SpuSaleAttributeCombination spuSaleAttributeCombination =
                findSpuSaleAttributeCombinationById(skuPostVm.spuSaleAttributeCombinationId());
        if(spuSaleAttributeCombination.getSku() != null){
            throw new BadRequestException(ErrorCodeConstant.SPU_SALE_ATTRIBUTE_COMBINATION_IN_USE
                    , spuSaleAttributeCombination.getId()
            );
        }
        checkGtinSkuExists(skuPostVm.gtin());
        Sku sku = Sku.builder()
                .price(skuPostVm.price())
                .gtin(skuPostVm.gtin())
                .code("")
                .attribute(spuSaleAttributeCombination)
                .build();
        Sku savedSku = skuRepository.save(sku);
        spuSaleAttributeCombination.setSku(savedSku);
        spuSaleAttributeCombinationRepository.save(spuSaleAttributeCombination);
        return SkuInfoGetVm.fromModel(sku);
    }
    public SkuInfoGetVm update(Long id, SkuPostVm skuPostVm){
        Sku sku = findSkuById(id);
        if(!skuPostVm.gtin().equals(sku.getGtin())){
            checkGtinSkuExists(skuPostVm.gtin());
        }
        sku.setGtin(skuPostVm.gtin());
        sku.setPrice(skuPostVm.price());
        skuRepository.save(sku);
        return SkuInfoGetVm.fromModel(sku);
    }
    private Sku findSkuById(Long skuId){
        return skuRepository.findById(skuId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SKU_ID_NOT_FOUND, skuId));
    }
    private Sku findSkuByGtin(String gtin){
        return skuRepository.findByGtin(gtin)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SKU_GTIN_NOT_FOUND, gtin));
    }
    private SpuSaleAttributeCombination findSpuSaleAttributeCombinationById(Long id){
        return spuSaleAttributeCombinationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SPU_SALE_ATTRIBUTE_COMBINATION_ID_NOT_FOUND, id));
    }
    private void checkGtinSkuExists(String gtin){
        if(skuRepository.existsByGtin(gtin)){
            throw new DuplicatedException(ErrorCodeConstant.SKU_GTIN_IS_DUPLICATED);
        }
    }
}

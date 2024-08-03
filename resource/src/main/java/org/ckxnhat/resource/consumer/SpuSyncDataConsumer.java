package org.ckxnhat.resource.consumer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.ckxnhat.resource.document.Spu;
import org.ckxnhat.resource.repository.search.SpuSearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-30 23:02:10.968
 */

@Service
@RequiredArgsConstructor
public class SpuSyncDataConsumer {
    private final SpuSearchRepository spuSearchRepository;
    @KafkaListener(topics = "${topic.name.spu}")
    public void listen(ConsumerRecord<?, ?> record) {
        if(record != null) {
            System.out.println(record.key());
            System.out.println(record.value());
            JsonObject data = new Gson().fromJson((String)record.value(), JsonObject.class);
            if(data != null){
                JsonObject payload = data.getAsJsonObject("payload");
                JsonObject after = payload.getAsJsonObject("after");
                String action = payload.get("op").getAsString();
                Long id = after.get("id").getAsLong();
                String name = after.get("name").getAsString();
                String thumbnailId = after.get("thumbnail_id").getAsString();
                String slug = after.get("slug").getAsString();
                Long categoryId = after.get("category_id").getAsLong();
                Long brandId = after.get("brand_id").getAsLong();
                Short sort = after.get("sort").getAsShort();
                Double price = after.get("min_price").getAsDouble();
                boolean isDeleted = after.get("is_deleted").getAsBoolean();
                Spu spu = new Spu(id, name, thumbnailId,slug,price,sort,brandId,categoryId,isDeleted);
                switch (action) {
                    case "c", "u": {
                        spuSearchRepository.save(spu);
                        break;
                    }
                    case "d": {
                        spuSearchRepository.deleteById(id);
                        break;
                    }
                }
            }
        }
    }
}

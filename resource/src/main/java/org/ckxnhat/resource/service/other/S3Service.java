package org.ckxnhat.resource.service.other;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 21:09:47.542
 */

@Service
@RequiredArgsConstructor
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.cloud_front.distribution_domain}")
    private String distributionDomain;
    @Value("${aws.cloud_front.public_key_id}")
    private String publicKeyId;
    @Value("${aws.cloud_front.name_private_key_file}")
    private String namePrivateKeyFile;
    @Value("${aws.cloud_front.ttl}")
    private int ttl;
    private final Tika tika = new Tika();
    private final RedisTemplate<String,String> redisTemplate;
    private final static String S3_KEY = "S3:";
    public String uploadImage(MultipartFile file){
        try {
            if(file.isEmpty()) return null;
            String objectKey = generateRandomName();
            ObjectMetadata md = new ObjectMetadata();
            md.setContentType(tika.detect(file.getOriginalFilename()));
            md.setContentLength(file.getSize());
            amazonS3.putObject(bucketName, objectKey, file.getInputStream(), md);
            return objectKey;
        }catch(Exception e) {
            return null;
        }
    }
    public List<String> uploadImages(List<MultipartFile> files){
        /*
        * async upload to s3 aws
        **/
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadImage(file)))
                .toList();

        /*
        * wait until all async job finish and return imageId
        **/
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
    public void deleteImage(String objectKey){
        if(objectKey == null || objectKey.isEmpty()) return;
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
    }
    public void deleteImages(String... objectKeys){
        if(objectKeys == null || objectKeys.length == 0) return;
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                .withKeys(objectKeys);
        amazonS3.deleteObjects(deleteObjectsRequest);
    }
    private String generateRandomName(){
        return UUID.randomUUID().toString().substring(0, 10) +
                System.currentTimeMillis();
    }
    public String getSignedUrl(String objectKey){
        try {
            if (objectKey == null) return null;

            String cacheKey = S3_KEY + objectKey;
            String result = redisTemplate.opsForValue().get(cacheKey);
            if (result == null) {
                ClassPathResource resource = new ClassPathResource(namePrivateKeyFile);
                result = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                        SignerUtils.Protocol.https,
                        distributionDomain,
                        resource.getFile(),
                        objectKey,
                        publicKeyId,
                        new Date(System.currentTimeMillis() + ttl)
                );
                redisTemplate.opsForValue().set(cacheKey, result, ttl, TimeUnit.MILLISECONDS);
            }

            return result;
        }catch (Exception e){
            return null;
        }
    }
}

package org.ckxnhat.resource.service.other;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.auth.aws.internal.signer.util.SignerUtils;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 21:09:47.542
 */

@Service
@RequiredArgsConstructor
public class S3Service {
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3AsyncClient s3AsyncClient;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.cloud_front.distribution_domain}")
    private String distributionDomain;
    @Value("${aws.cloud_front.public_key_id}")
    private String publicKeyId;
    @Value("${aws.cloud_front.name_private_key_file}")
    private String namePrivateKeyFile;
    @Value("${aws.cloud_front.ttl}")
    private long ttl;
    @Value("${aws.bucket.multipart_upload.part_size}")
    private int partSize;
    private final Tika tika = new Tika();
    private final RedisTemplate<String,String> redisTemplate;
    private final static String S3_KEY = "S3:";

    public String uploadImage(MultipartFile file){
        try {
            if(file.isEmpty()) return null;
            String objectKey = generateRandomName();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(tika.detect(file.getOriginalFilename()))
                    .contentLength(file.getSize())
                    .build();
            ByteBuffer byteBuffer = ByteBuffer.wrap(file.getBytes());
            s3Client.putObject(request, RequestBody.fromByteBuffer(byteBuffer));
            return objectKey;
        }catch(Exception e) {
            return null;
        }
    }
    public List<String> uploadImages(List<MultipartFile> files){
        ExecutorService executorService = Executors.newFixedThreadPool(files.size());
        try{
            List<Callable<String>> callables = files.stream()
                    .map(file -> (Callable<String>) () -> uploadImage(file))
                    .toList();
            List<Future<String>> futures = executorService.invokeAll(callables);
            return futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .toList();
        }
        catch(Exception e){
            return Collections.emptyList();
        }
        finally{
            executorService.shutdown();
        }
    }
    public void deleteImage(String objectKey){
        if(objectKey == null || objectKey.isEmpty()) return;
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        s3Client.deleteObject(request);
    }
    public void deleteImages(String... objectKeys){
        if(objectKeys == null || objectKeys.length == 0) return;
        List<ObjectIdentifier> toDelete = new ArrayList<>();
        for(String objectKey : objectKeys){
            toDelete.add(ObjectIdentifier.builder()
                    .key(objectKey)
                    .build()
            );
        }
        DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder()
                        .objects(toDelete)
                        .build()
                )
                .build();
        s3Client.deleteObjects(request);
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
                String baseUrl = "https://" + objectKey + "/" +objectKey;
                CannedSignerRequest request = CannedSignerRequest.builder()
                        .resourceUrl(baseUrl)
                        .keyPairId(publicKeyId)
                        .privateKey(Paths.get(resource.getPath()))
                        .expirationDate(Instant.now().plus(ttl, ChronoUnit.MILLIS))
                        .build();

                result = CloudFrontUtilities.create()
                        .getSignedUrlWithCannedPolicy(request).url();
                redisTemplate.opsForValue().set(cacheKey, result, ttl, TimeUnit.MILLISECONDS);
            }

            return result;
        }catch (Exception e){
            return null;
        }
    }
    private String uploadBigFile(MultipartFile file){
        if(file.isEmpty()) return null;
        String objectKey = generateRandomName();
        CreateMultipartUploadResponse createMultipartUploadResponse =
                s3Client.createMultipartUpload(b -> b
                        .bucket(bucketName)
                        .key(objectKey)
                );
        String uploadId = createMultipartUploadResponse.uploadId();
        List<CompletedPart> completedParts = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
        try(InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[partSize];
            int bytesRead;
            int partNumber = 1;
            List<Future<CompletedPart>> futures = new ArrayList<>();
            while((bytesRead = inputStream.read(buffer)) != -1){
                final int currentPartNumber = partNumber;
                final byte[] currentBuffer = Arrays.copyOf(buffer, bytesRead);
                Callable<CompletedPart> task = () -> {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(currentBuffer);
                    UploadPartRequest request = UploadPartRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .uploadId(uploadId)
                            .partNumber(currentPartNumber)
                            .build();
                    UploadPartResponse response = s3Client.uploadPart(request, RequestBody.fromByteBuffer(byteBuffer));
                    return CompletedPart.builder()
                            .partNumber(currentPartNumber)
                            .eTag(response.eTag())
                            .build();
                };
                futures.add(executorService.submit(task));
                partNumber++;
            }
            for(Future<CompletedPart> future: futures){
                try{
                    completedParts.add(future.get());
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
            s3Client.completeMultipartUpload(b -> b
                    .bucket(bucketName)
                    .key(objectKey)
                    .uploadId(uploadId)
                    .multipartUpload(
                            CompletedMultipartUpload.builder()
                                    .parts(completedParts)
                                    .build()
                    )
            );
            return objectKey;
        }catch (Exception e){
            s3Client.abortMultipartUpload(b -> b
                    .bucket(bucketName)
                    .key(objectKey)
                    .uploadId(uploadId)
            );
            return null;
        }
        finally{
            executorService.shutdown();
        }
    }
}

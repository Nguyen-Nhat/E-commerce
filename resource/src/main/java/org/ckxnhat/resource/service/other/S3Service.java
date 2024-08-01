package org.ckxnhat.resource.service.other;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-18 21:09:47.542
 */

@Service
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
    public String uploadImage(MultipartFile file) throws IOException {
        String objectKey = generateRandomName();
        ObjectMetadata md = new ObjectMetadata();
        md.setContentType(tika.detect(file.getOriginalFilename()));
        md.setContentLength(file.getSize());
        amazonS3.putObject(bucketName, objectKey, file.getInputStream(), md);
        return objectKey;
    }
    public void deleteImage(String objectKey){
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
    }
    private String generateRandomName(){
        return UUID.randomUUID().toString().substring(0, 10) +
                System.currentTimeMillis();
    }

    public String getSignedUrl(String objectKey){
        try {
            ClassPathResource resource = new ClassPathResource(namePrivateKeyFile);
            return CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                    SignerUtils.Protocol.https,
                    distributionDomain,
                    resource.getFile(),
                    objectKey,
                    publicKeyId,
                    new Date(System.currentTimeMillis() + ttl)
            );
        }catch (Exception e){
            return null;
        }
    }

}

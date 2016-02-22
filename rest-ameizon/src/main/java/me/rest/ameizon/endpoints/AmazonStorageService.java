package me.rest.ameizon.endpoints;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;

/**
 * An amazon AWS S3 storage service consumer to upload file objects into the
 * cloud storage.
 *
 * @author Akis Papadopoulos
 */
public class AmazonStorageService {

    // Storage bucket
    private final String bucket;

    // Bucket region name
    private final String regionName;

    // Folder path within bucket
    private final String folderPath;

    // Service authentication access key
    private final String accessKey;

    // Service authentication secret key
    private final String secretKey;

    /**
     * A constructor initiating a S3 storage service given the bucket info plus
     * the authentication credential data.
     *
     * @param bucket the bucket name.
     * @param regionName the region name of the bucket.
     * @param folderPath the relative path within the bucket.
     * @param accessKey the service authentication access key.
     * @param secretKey the service authentication secret key.
     */
    public AmazonStorageService(String bucket, String regionName, String folderPath, String accessKey, String secretKey) {
        this.bucket = bucket;
        this.regionName = regionName;
        this.folderPath = folderPath;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * A method posting the given file object to the amazon storage cloud.
     *
     * @param file the file to post.
     * @return the MD5 token of the file after posted.
     * @throws Exception throws unknown errors.
     */
    public String put(File file) throws Exception {
        // Setting up the client populated with the credentials
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3client = new AmazonS3Client(credentials);

        // Defining the region on which the bucket is created
        Region region = Region.getRegion(Regions.valueOf(regionName));
        s3client.setRegion(region);

        // TMP
        System.out.println("Uploading a new object to S3 from a file '" + file.getName() + "'");

        // Sending the object put request given a public read access
        PutObjectRequest por = new PutObjectRequest(bucket, folderPath + "/" + file.getName(), file);
        por.withCannedAcl(CannedAccessControlList.PublicRead);

        // TMP
        long s = System.currentTimeMillis();

        PutObjectResult result = s3client.putObject(por);

        // TMP
        System.out.println("Tm: " + (System.currentTimeMillis() - s) / 1000.0);
        System.out.println("MD5: " + result.getContentMd5().toUpperCase());

        return result.getContentMd5();
    }
}

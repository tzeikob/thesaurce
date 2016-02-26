package me.rest.ameizon.endpoints;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;
import java.io.InputStream;

/**
 * An amazon AWS S3 storage service consumer to upload file objects into the
 * cloud storage.
 *
 * @author Akis Papadopoulos
 */
public class AmazonStorageService {

    // Storage bucket
    private final String bucket;

    // Folder path within bucket
    private final String folderPath;

    // Storage service S3 client
    private final AmazonS3 client;

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
        this.folderPath = folderPath;

        // Setting up the service client instance
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        client = new AmazonS3Client(credentials);

        // Defining the region on which the bucket is attached
        Region region = Region.getRegion(Regions.valueOf(regionName));
        client.setRegion(region);
    }

    /**
     * A method posting the given file object to the amazon storage cloud.
     *
     * @param file the file to post.
     * @return the MD5 token of the file after posted.
     * @throws Exception throws unknown errors.
     */
    public String put(File file) throws Exception {
        // Sending the object put request given a public read access
        PutObjectRequest object = new PutObjectRequest(bucket, folderPath + "/" + file.getName(), file);
        object.withCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult result = client.putObject(object);

        return result.getContentMd5();
    }

    /**
     * A method posting the given input stream given the stream bytes as also
     * the metadata.
     *
     * @param inputStream the input stream to post.
     * @param contentLength the content length of the stream.
     * @param fileName the file name.
     * @param contentType the content type of the file.
     * @return the MD5 token of the file after posted.
     * @throws Exception throws unknown errors.
     */
    public String put(InputStream inputStream, long contentLength, String fileName, String contentType) throws Exception {
        // Setting the metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);

        // Sending the object put request given a public read access
        PutObjectResult result = client.putObject(bucket, folderPath + "/" + fileName, inputStream, metadata);

        return result.getContentMd5();
    }
}

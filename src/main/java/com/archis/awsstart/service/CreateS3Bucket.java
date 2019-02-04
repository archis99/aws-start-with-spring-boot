/**
 * 
 */
package com.archis.awsstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;

/**
 * @author Archisman
 *
 */
@Component
public class CreateS3Bucket implements CommandLineRunner {
	
	@Autowired
    AmazonS3 amazonS3;
	
	static String bucketName = "first-bucket-with-spring-boot" ;

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Creating S3 bucket: "+bucketName);
        if(amazonS3.doesBucketExist(bucketName)) {
        	System.out.println("Bucket name is not available."
              + " Try again with a different Bucket name.");
            return;
        }
        amazonS3.createBucket(bucketName);
        System.out.println("Bucket created successfully "+bucketName);
		
	}

}

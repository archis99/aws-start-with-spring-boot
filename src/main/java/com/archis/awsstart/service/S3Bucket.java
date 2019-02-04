/**
 * 
 */
package com.archis.awsstart.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

/**
 * @author Archisman
 *
 */
@Component
public class S3Bucket {

	@Autowired
    AmazonS3 amazonS3;
	
	@Autowired
    ResourceLoader resourceLoader;
	
	static String bucketName = "first-bucket-with-spring-boot" ;
	
	public void uploadObject(String bucketName, File fileName) {
        
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "superheroes.json", fileName);
        amazonS3.putObject(putObjectRequest);
    }
	
	@SuppressWarnings("unchecked")
	public void updateObject(String bucketName, String key, 
			String superhero, String universe) throws ParseException, IOException {
		
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        S3Object s3Object = amazonS3.getObject(getObjectRequest);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        
        JSONParser parser = new JSONParser();
	    File targetFile = new File("src/main/resources/superheroes.json");
	    FileUtils.copyInputStreamToFile(objectInputStream, targetFile);
        	    
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(targetFile));
        JSONArray superheroes = null;
        JSONObject jo = null;
        System.out.println(jsonObject);
        superheroes = (JSONArray) jsonObject.get("superheroes");
        
        for (Object obj : superheroes)
        {
          jo = (JSONObject) obj;
          String superheroName = (String) jo.get("name");
          if (superheroName.equals(superhero)) {
        	  jo.put("universe", universe);
        	  break;
          }
        }
        System.out.println("Modified object: "+ jsonObject);
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/superheroes.json"));
        writer.write(jsonObject.toString());
        writer.close();
        
        uploadObject(bucketName, new File("src/main/resources/superheroes.json"));
        
    }
	
	public JSONArray onlyDownloadObject(String bucketName, String key) throws ParseException, IOException {
		
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);

        S3Object s3Object = amazonS3.getObject(getObjectRequest);

        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        
        JSONParser parser = new JSONParser();
        
	    File targetFile = new File("src/main/resources/superheroes.json");
	 
	    FileUtils.copyInputStreamToFile(objectInputStream, targetFile);
        	    
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(targetFile));
        
        JSONArray superheroes = null;
        
        System.out.println(jsonObject);
        
        superheroes = (JSONArray) jsonObject.get("superheroes");
        
        return superheroes;
    }
	
	public JSONObject downloadAParticularObject(String bucketName, String key, 
			String superhero) throws ParseException, IOException {

		String superheroName = null;
        JSONObject jo = null;
        JSONObject returnedJO = null;
		
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);

        S3Object s3Object = amazonS3.getObject(getObjectRequest);

        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        
        JSONParser parser = new JSONParser();
        
	    File targetFile = new File("src/main/resources/superheroes.json");
	 
	    FileUtils.copyInputStreamToFile(objectInputStream, targetFile);
        	    
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(targetFile));
        
        JSONArray superheroes = null;
        
        System.out.println(jsonObject);
        
        superheroes = (JSONArray) jsonObject.get("superheroes");
        
        for (Object obj : superheroes)
        {
          
          jo = (JSONObject) obj;
          
          superheroName = (String) jo.get("name");
          if (superheroName.equals(superhero)) {
        	  returnedJO = jo;
        	  break;
          }
        }
        
        return returnedJO;
    }
	
	
}

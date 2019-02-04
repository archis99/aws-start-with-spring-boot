/**
 * 
 */
package com.archis.awsstart.service;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Archisman
 *
 */
@RestController
public class RESTService {
	
	@Autowired
	S3Bucket s3bucket;
	
	@Autowired
    ResourceLoader resourceLoader;
	
	static String bucketName = "first-bucket-with-spring-boot" ;
	
	@RequestMapping("/upload")
	public void upload() throws IOException {
		
		Resource fileResource = resourceLoader.getResource("classpath:superheroes.json");
		
		s3bucket.uploadObject(bucketName, fileResource.getFile());
		
	}
	
	@RequestMapping("/update/{superhero}/{universe}")
	public void update(@PathVariable String superhero, 
			@PathVariable String universe) throws IOException, ParseException {
		
		//Resource fileResource = resourceLoader.getResource("classpath:superheroes.json");
		s3bucket.updateObject(bucketName, "superheroes.json",
				superhero, universe);
		
	}
	
	@RequestMapping("/fetchDetails")
	public JSONArray fetchDetails() throws IOException, ParseException {
		
		JSONArray superheroes = 
				s3bucket.onlyDownloadObject(bucketName, "superheroes.json");
		
		return superheroes;
		
	}
	
	@RequestMapping("/fetchDetails/{superhero}")
	public JSONObject fetchParticularDetails(@PathVariable String superhero) throws IOException, ParseException {
		
		JSONObject superheroDetails = 
				s3bucket.downloadAParticularObject(bucketName, "superheroes.json",
						superhero);
		
		return superheroDetails;
		
	}
}

package com.axisbank.project2.service.amazons3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.axisbank.project2.encryption.EncryptDecrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;


@Service
public class StorageService {
	@Value("${application.bucket.name}")
    private String bucketName;
	
	@Value("${application.bucket.optional.name}")
	private String optionalBucketName;
	
	private EncryptDecrypt aes=new EncryptDecrypt();
	
	private static final Logger log = LoggerFactory.getLogger(StorageService.class);
	
    @Autowired
    private AmazonS3 s3Client;
    
    @Autowired
    private AmazonS3 s3ClientOptional;

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded : " + fileName;
    }
    
    public String upoadEncryptedFile(MultipartFile file) {
    	 File fileObj = convertMultiPartFileToFile(file);
    	 String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    	 try {
			FileInputStream fis =new FileInputStream(fileObj);
			String encrypt=aes.encryptFile(fis);
			FileOutputStream fos=new FileOutputStream(fileObj);
			byte[] data= encrypt.getBytes();
			fos.write(data);
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "File uploaded : "+ fileName;
    }
    
    public String uploadEncryptedReport(File file) {
    	File fileObj = file;
    	String fileName=file.getName();
    	try {
			FileInputStream fis =new FileInputStream(fileObj);
			String encrypt=aes.encryptFile(fis);
			FileOutputStream fos=new FileOutputStream(fileObj);
			byte[] data= encrypt.getBytes();
			fos.write(data);
			s3ClientOptional.putObject(new PutObjectRequest(optionalBucketName, fileName, fileObj));
	        fileObj.delete();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "File uploaded : "+ fileName;
    	
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public byte[] downloadDecryptFile(String fileName) {
    	 S3Object s3Object = s3Client.getObject(bucketName, fileName);
         S3ObjectInputStream inputStream = s3Object.getObjectContent();
         BufferedInputStream bis=new BufferedInputStream(inputStream);
         try {
			byte[] content = IOUtils.toByteArray(bis);
			return aes.decryptByteArray(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return null;        
    }
    
    public byte[] downloadDecryptReport(String fileName) {
   	 S3Object s3Object = s3ClientOptional.getObject(optionalBucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        BufferedInputStream bis=new BufferedInputStream(inputStream); 
        try {
			byte[] content = IOUtils.toByteArray(bis);
			return aes.decryptByteArray(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
      
            
   }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }
    
    public String deleteReport(String fileName) {
        s3ClientOptional.deleteObject(optionalBucketName, fileName);
        return fileName + " removed ...";
    }
    
    


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
    
    
    public List<String> listAllFiles() {

        ListObjectsV2Result listObjectsV2Result = s3Client.listObjectsV2(bucketName);
      return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());

    }
    
    public List<String> listAllReports() {

        ListObjectsV2Result listObjectsV2Result = s3ClientOptional.listObjectsV2(optionalBucketName);
      return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }
    
}
package com.axisbank.project2.controlller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.axisbank.project2.report.model.ReportModel;
import com.axisbank.project2.repository.ReportModelRepository;
import com.axisbank.project2.service.amazons3.StorageService;
import com.axisbank.project2.service.reports.ReportGenerationService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/files3")
@CrossOrigin("http://localhost:3000")
public class StorageController {
	
	@Autowired
	private ReportGenerationService reportService;
	
	String downloadDecryptedFileName = "";

    @Autowired
    private StorageService service;
    
    @Autowired
    private ReportModelRepository repository;

    @PostMapping("/uploads3")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }
    
    @PostMapping("/encrypt/upload")
    public ResponseEntity<String> uploadEncryptFile(@RequestParam(value = "file") MultipartFile file){
    	return new ResponseEntity<>(service.upoadEncryptedFile(file),HttpStatus.OK);
    }
    

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
    
    @GetMapping("/decrypt/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadDecryptFile(@PathVariable String fileName)
    {
    	downloadDecryptedFileName+=" "+fileName;
    	byte[] data=service.downloadDecryptFile(fileName);
    	ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
    
    @GetMapping("/decryptReport/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadDecryptReport(@PathVariable String fileName)
    {
    	downloadDecryptedFileName+=" "+fileName;
    	byte[] data=service.downloadDecryptReport(fileName);
    	ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
    
    @PostMapping("/generateReport")
    public ResponseEntity<String> uploadReport(@RequestBody ReportModel report){
    	
    	ReportModel r = new ReportModel();
    	r.setUsername(report.getUsername());
    	r.setEmail(report.getEmail());
    	r.setDownloadedItems(downloadDecryptedFileName);
    	long t=System.currentTimeMillis();
    	r.setTimestamp(""+t);
    	repository.save(r);
    	try {
			File pdf=reportService.exportReport(r.getUsername());
			String s=service.uploadEncryptedReport(pdf);
			return new ResponseEntity<>(s,HttpStatus.OK);
		} catch (JRException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    	
    	
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/report/{fileName}")
    public ResponseEntity<String> deleteReport(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteReport(fileName), HttpStatus.OK);
    }
    
    
    
    
    @GetMapping("/list")
    public List<String> getAllfile(){
    	return service.listAllFiles();
    }
    
    @GetMapping("/listReport")
    public List<String> getAllReport(){
    	return service.listAllReports();
    }
}
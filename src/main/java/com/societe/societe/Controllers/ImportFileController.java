package com.societe.societe.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.societe.societe.Services.ImportFileService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.societe.societe.Models.Client;
import com.societe.societe.Models.ImportFile;
import com.societe.societe.Repositories.ClientRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ImportFileController {
    @Autowired
    private ImportFileService fileSerivce;

    @Autowired
    ClientRepository client_repos;
    @PostMapping("/import")
   public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file){

    fileSerivce.save(file);
    return ResponseEntity.ok("Fichier enregister avec succes ");
   }

   @GetMapping("/clients")
   public Iterable<Client> getClients(){
    return client_repos.findAll();
   }
   
    

   @GetMapping("/statistique")
   public String GetStatistique() {
    
   return fileSerivce.getMoyenne();
     
       
   }
   
}

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
;;
@CrossOrigin(origins = "http://localhost:4200/")
@RestController
public class ImportFileController {
    @Autowired
    private ImportFileService fileSerivce;

    @Autowired
    ClientRepository client_repos;
    @PostMapping("/import")
   public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file){

    fileSerivce.save(file);
    return ResponseEntity.ok("Fichier enregister avec succes "+file.getOriginalFilename());
   }

   @GetMapping("/clients")
   public Iterable<Client> getClients(){
    return client_repos.findAll();
   }
   
   @GetMapping("files")
   public ResponseEntity<List<ImportFile>> getAllFile(){

    List<ImportFile> files=fileSerivce.loadAll().map(
        path->{
        String filename=path.getFileName().toString();
        String url=MvcUriComponentsBuilder.fromMethodName(ImportFileController.class,"getFile",path.getFileName().toString()).build().toString();
        return new ImportFile(filename,url);
    }
    ).collect(Collectors.toList());
return ResponseEntity.ok(files);     
   }

   @GetMapping("files/{filename:.+}")
   public ResponseEntity<?> getFile(@PathVariable String filename){
    Resource file=fileSerivce.load(filename);
    return ResponseEntity.ok()
    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+file.getFilename()+"").body(file);

   }

   @GetMapping("/statistique")
   public String GetStatistique() {
    String stat="";
    Iterable<Client>les_clients=this.client_repos.findAll();
    int i=0;
    List<String>liste_prof=new ArrayList<>();
    for (Client client : les_clients) {
        if(!liste_prof.contains(client.getProfession())){
             liste_prof.add(client.getProfession());
        }
       

    }

     for (String profession : liste_prof) {
        float som_salaire=0;
        int nbre=0;
        for (Client client : les_clients){
            if(client.getProfession().equals(profession)){
                som_salaire+=client.getSalaire();
                nbre++;
            }
         }
         Float moyenne=som_salaire/nbre;
        stat+=profession+"--->"+moyenne+"\n";
    }
     System.out.println(stat);
    
    return stat;
       
   }
   
}

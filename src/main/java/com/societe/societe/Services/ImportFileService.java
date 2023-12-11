package com.societe.societe.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.stream.XMLStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import  com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;
 
import com.societe.societe.Models.Client;
import com.societe.societe.Models.RowClient;
import com.societe.societe.Repositories.ClientRepository;

@Service
public class ImportFileService implements FileStorageService{

    @Autowired
    ClientRepository clients_repos;
    
    private final Path root=Paths.get("uploads");
private final ObjectMapper objMp;
    public ImportFileService(ObjectMapper obj)
    {
        this.objMp=obj;
    }


    @Override
    public void init() {
       try {
        Files.createDirectories(root);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       
    }

    @Override
    public void save(MultipartFile file) {
        //on test voir si le repertoire existe, sinon on cree le repertoire dans la methode init
        init();
        //on fait une copie du fichier dans le repertoire
        
        try {
            
            Files.copy(file.getInputStream(),this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //lecture du fichier

        //enregistrement dans la bd via le model
        
        String nom=file.getOriginalFilename();
        
        String chemin=this.root.toString()+"/"+file.getOriginalFilename();
        System.out.println("le chemin est "+chemin);
        String extension=nom.substring(nom.indexOf(".")+1);
        List<Client> all_clients=new ArrayList<>();
        
        //lecture de fichier xlsx
        if(extension.equals("txt"))
        {
            System.out.println("Fichie text");
            try {
                // Création d'un fileReader pour lire le fichier
                FileReader fileReader = new FileReader(chemin);
                
                // Création d'un bufferedReader qui utilise le fileReader
                BufferedReader reader = new BufferedReader(fileReader);
                
                // une fonction à essayer pouvant générer une erreur
                String line = reader.readLine();
                
                while (line != null) {
                    // affichage de la ligne
                   // all_clients.add(new Client(line.toString().split(",")[0],line.toString().split(",")[1],line.toString().split(",")[3],Float.valueOf(line.toString().split(",")[2]),Float.valueOf(line.toString().split(",")[4])));
                    clients_repos.save(new Client(line.toString().split(",")[0],line.toString().split(",")[1],line.toString().split(",")[3],Float.valueOf(line.toString().split(",")[2]),Float.valueOf(line.toString().split(",")[4])));
                    // lecture de la prochaine ligne
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            System.err.println(all_clients);
            
        }
        else
            {
                //lecture de fichie json
                if(extension.equals("json"))
                {
                      System.out.println("Fichier Json");
                      JsonNode json;
                        try
                        (InputStream input_stream=file.getInputStream()) 
                        {
                        
                             json=this.objMp.readValue(input_stream, JsonNode.class);
                                                         

                        } catch (Exception e) {
                            e.printStackTrace();
                           throw new RuntimeException("Failed to read json file");
                        }                               
                        JsonNode clients=getClientFromJson(json);
                        for(JsonNode client:clients)
                            {
                                clients_repos.save(new Client(client.get("nom").asText(),client.get("prenom").asText(),client.get("profession").asText(),client.get("age").asLong(),client.get("salaire").asLong()));
                               // all_clients.add(new Client(client.get("nom").asText(),client.get("prenom").asText(),client.get("profession").asText(),client.get("age").asLong(),client.get("salaire").asLong()));
                            }
                            
                }
                
                else
                    {
                        //lecture de fichier txt
                        if(extension.equals("csv"))
                        {
                            System.out.println("Fichier Csv");
                             
                            try (BufferedReader br = new BufferedReader(new FileReader(chemin))) 
                            {
                                String line;
                                while( (line=br.readLine())!=null){
                                    System.out.println(line);
                                   
                                     String []tab=line.split(";");
                                    clients_repos.save(new Client(String.valueOf(tab[0]), String.valueOf(tab[1]),String.valueOf(tab[3]),Float.valueOf(tab[2]),Float.valueOf(tab[4])));
                                    
                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                System.err.println("Erreur de lecture de fichier");
                            }

                        }
                        //lecture de fichier xmls
                        else  if(extension.equals("xml"))
                        {
                            System.out.println("Fichier XMLS");

                           RowClient row=new RowClient();
                          List<Client> ls= row.read((new File(chemin)));
                            
                        }
                    }
            }
    }
    private JsonNode getClientFromJson(JsonNode json){
        return Optional.ofNullable(json)
        .map(client->client.get("client")).orElseThrow(()->new IllegalArgumentException("Invalide Json Format"));
        
    }

    @Override
    public Resource load(String filename) {
        
        Path file=root.resolve(filename);
        Resource resource=null;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(resource.exists() || resource.isReadable()){
             
             return resource;
        }
        else{
            throw new RuntimeException("This files is not readable");
        }
        
    
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public Stream<Path> loadAll() {
        
        try {
            return Files.walk(this.root,1).filter(path-> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Une erreur s est produite lors de la lecture");
        }

    }
    
}

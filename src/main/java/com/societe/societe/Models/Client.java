package com.societe.societe.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    String nom, prenom, profession;
    float  age, salaire;
    public Client(String nom,String prenom,String profession, float age,float salaire){
        this.nom=nom;
        this.prenom=prenom;
        this.profession=profession;
        this.age=age;
        this.salaire=salaire;
    }
}

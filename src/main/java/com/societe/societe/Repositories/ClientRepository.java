package com.societe.societe.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.societe.societe.Models.Client;

public interface  ClientRepository extends CrudRepository<Client, Long> {
    
}

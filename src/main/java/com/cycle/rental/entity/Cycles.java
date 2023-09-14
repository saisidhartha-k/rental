package com.cycle.rental.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Cycles {
    
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cycleId;
    
    private int stock;
    private String cycleName;
    private int numBorrowed;
    public  int getNumAvailable() {
        return stock - numBorrowed;
    }
  

    
}
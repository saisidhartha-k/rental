package com.cycle.rental.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

    @Entity
    @Data
public class BorrowedCycles {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int borrowedId;
    
    private String cycleName;

}

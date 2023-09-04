package com.cycle.rental.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

    @Entity
    @Data
public class CyclesStock {
  
    @Id
    private int cycleId;
    
    @ColumnDefault("1")
    private int stock; 
    private String cycleName;
  

    
}
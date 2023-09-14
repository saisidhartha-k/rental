package com.cycle.rental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cycle.rental.entity.BorrowedCycles;
import com.cycle.rental.entity.Cycles;
import com.cycle.rental.entity.User;
import com.cycle.rental.repository.BorrowedCyclesRepository;
import com.cycle.rental.repository.CyclesRepository;
import com.cycle.rental.repository.UserRepository;

import jakarta.annotation.security.RolesAllowed;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cycles")
public class CyclesController  {

    @Autowired
    private CyclesRepository cyclesRepository;

    @Autowired
    private BorrowedCyclesRepository borrowedCyclesRepository;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
     //@RolesAllowed("ROLE_ADMIN")
      //  @Role("ADMIN")
     // @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public Iterable<Cycles> getCyclesList(Principal principal,Authentication authentication) {
        // Jwt jwt = (jwt) authentication.getprincipal();
        return cyclesRepository.findAll();
    }

    @PostMapping("/borrow/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public BorrowedCycles borrowCycle(@PathVariable int id) {
        Optional<Cycles> cycleOptional = cyclesRepository.findById(id);

        if (cycleOptional.isPresent()) {
            Cycles borrowedCycle = cycleOptional.get();

            if (borrowedCycle.getStock() != 0) {
                borrowedCycle.setStock(borrowedCycle.getStock() - 1);
            }
            Optional<BorrowedCycles> borrowedOptional = borrowedCyclesRepository.findById(id);

            if (borrowedOptional.isPresent()) {
                BorrowedCycles borrowedCycleEntity = borrowedOptional.get();
                borrowedCycleEntity.setStock(borrowedCycleEntity.getStock() + 1);
               return  borrowedCyclesRepository.save(borrowedCycleEntity);
            } else {
                BorrowedCycles borrowedEntity = new BorrowedCycles();
                borrowedEntity.setBorrowedCycleId(borrowedCycle.getCycleId());
                borrowedEntity.setCycleName(borrowedCycle.getCycleName());
                borrowedEntity.setStock(borrowedEntity.getStock() + 1);
                return borrowedCyclesRepository.save(borrowedEntity);
            }
        }
        return null;
    }

    @PostMapping("/return/{id}")
    public Cycles returnCycle(@PathVariable int id) {
        Optional<BorrowedCycles> cycleOptional = borrowedCyclesRepository.findById(id);

        if (cycleOptional.isPresent()) {
            BorrowedCycles borrowedCycle = cycleOptional.get();

            if (borrowedCycle.getStock() != 0) {
                borrowedCycle.setStock(borrowedCycle.getStock() - 1);

                if (borrowedCycle.getStock() == 0) {
                    borrowedCyclesRepository.delete(borrowedCycle);
                }

                Optional<Cycles> returnedCycleOptional = cyclesRepository.findById(borrowedCycle.getBorrowedCycleId());

                if (returnedCycleOptional.isPresent()) {
                    Cycles returnedCycleEntity = returnedCycleOptional.get();
                    returnedCycleEntity.setStock(returnedCycleEntity.getStock() + 1);
                    return cyclesRepository.save(returnedCycleEntity);
                }
            }
        }
        return null;
    }

    @PutMapping("/add")
    public Cycles addCycle(@RequestBody Cycles newCycle) {
       return cyclesRepository.save(newCycle);
    }

    @GetMapping("/restock")
       // @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public Iterable<Cycles> getCyclesStock() {
        return cyclesRepository.findAll();
    }

    @PutMapping("/addStock/{id}")
    public Cycles addStock(@PathVariable int id) {
        Optional<Cycles> cycleOptional = cyclesRepository.findById(id);

        if (cycleOptional.isPresent()) {
            Cycles cycle = cycleOptional.get();
            cycle.setStock(cycle.getStock() + 1);
            return cyclesRepository.save(cycle);
        }
        return null;
    }

    @GetMapping("/borrowed")

    public List<BorrowedCycles> getBorrowedList() {
        return borrowedCyclesRepository.findAll();
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User newUser)
    {
        return userRepository.save(newUser);
    }

    @GetMapping("/Users")
     public Iterable<User> showUsers() {
        return userRepository.findAll();
    }

}

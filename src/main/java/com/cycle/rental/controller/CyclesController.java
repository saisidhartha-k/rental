package com.cycle.rental.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cycle.rental.entity.BorrowedCycles;
import com.cycle.rental.entity.Cycles;
import com.cycle.rental.repository.BorrowedCyclesRepository;
import com.cycle.rental.repository.CyclesRepository;

@Controller
public class CyclesController {

    @Autowired
    private CyclesRepository cyclesRepository;

    @Autowired
    private BorrowedCyclesRepository borrowedCyclesRepository;

    @GetMapping("/cycles")
    public String getCyclesList(Model model) {
        model.addAttribute("cycles", cyclesRepository.findAll());
        return "display";
    }

    @PostMapping("/cycles/borrow")
    public String borrowCycle(@RequestParam int cycleId) {
        Optional<Cycles> cycleOptional = cyclesRepository.findById(cycleId);
        
        if (cycleOptional.isPresent()) {
        Cycles borrowedCycle = cycleOptional.get();
        cyclesRepository.delete(borrowedCycle); 
        
        BorrowedCycles borrowedEntity = new BorrowedCycles();
        borrowedEntity.setCycleName(borrowedCycle.getCycleName());
        borrowedCyclesRepository.save(borrowedEntity); 
    }
        return "redirect:/cycles/borrowed"; 
    }

     @PostMapping("/cycles/return")
    public String returnCycle(@RequestParam int cycleId) {
        Optional<BorrowedCycles> cycleOptional = borrowedCyclesRepository.findById(cycleId);
        
        if (cycleOptional.isPresent()) {
        BorrowedCycles borrowedCycle = cycleOptional.get();
        borrowedCyclesRepository.delete(borrowedCycle);
        
        Cycles cycleEntity = new Cycles();
        cycleEntity.setCycleName(borrowedCycle.getCycleName());
        cyclesRepository.save(cycleEntity); 
    }
        return "redirect:/cycles"; 
    }

    @GetMapping("/cycles/borrowed")
    public String getBorrowedList(Model model) {
        model.addAttribute("borrowed", borrowedCyclesRepository.findAll());
        return "borrowedCycles";
    }
}

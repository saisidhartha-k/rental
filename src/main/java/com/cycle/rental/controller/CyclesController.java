package com.cycle.rental.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cycle.rental.entity.BorrowedCycles;
import com.cycle.rental.entity.Cycles;
import com.cycle.rental.repository.BorrowedCyclesRepository;
import com.cycle.rental.repository.CyclesRepository;
import com.cycle.rental.repository.CyclesStockRepository;

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

        if(borrowedCycle.getStock() != 0)
        {
            borrowedCycle.setStock(borrowedCycle.getStock() - 1);     
        }
        Optional<BorrowedCycles> borrwedOptional = borrowedCyclesRepository.findById(cycleId);

            if(borrwedOptional.isPresent())
            {
                BorrowedCycles borrowedCycleEntity = borrwedOptional.get();
                borrowedCycleEntity.setStock(borrowedCycleEntity.getStock() + 1);
                borrowedCyclesRepository.save(borrowedCycleEntity);

            }
            else{
            BorrowedCycles borrowedEntity = new BorrowedCycles();
            borrowedEntity.setBorrowedCycleId(borrowedCycle.getCycleId());
            borrowedEntity.setCycleName(borrowedCycle.getCycleName());
            borrowedEntity.setStock(borrowedEntity.getStock() + 1);
            borrowedCyclesRepository.save(borrowedEntity); 
            }
    }
        return "redirect:/cycles/borrowed"; 
    }

    @PostMapping("/cycles/return")
    public String returnCycle(@RequestParam int cycleId) {
        Optional<BorrowedCycles> cycleOptional = borrowedCyclesRepository.findById(cycleId);
    
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
                    cyclesRepository.save(returnedCycleEntity);
                }
            }
        }
        return "redirect:/cycles";
    }
    

    @PostMapping("/cycles/add")
    public String addCycle(@RequestParam String cycleName, @RequestParam int cycleId, @RequestParam int cyclestock) {
        Cycles newCycle = new Cycles();
        newCycle.setCycleId(cycleId);
        newCycle.setCycleName(cycleName);
        newCycle.setStock(cyclestock);
        cyclesRepository.save(newCycle);
        
        return "redirect:/cycles";
    }

    @GetMapping("/cycles/restock")
    public String CyclesStock(Model model) {
        model.addAttribute("cycles", cyclesRepository.findAll());
        return "restockCycles";
    }

    @GetMapping("/cycles/borrowed")
    public String getBorrowedList(Model model) {
        model.addAttribute("borrowed", borrowedCyclesRepository.findAll());
        return "borrowedCycles";
    }

  
}

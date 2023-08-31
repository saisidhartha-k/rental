package com.cycle.rental.controller;



import com.cycle.rental.entity.Cycles;
import com.cycle.rental.repository.CyclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CyclesController {

    private final CyclesRepository cyclesRepository;

    @Autowired
    public CyclesController(CyclesRepository cyclesRepository) {
        this.cyclesRepository = cyclesRepository;
    }

    @GetMapping("/cycles")
    public String getCyclesList(Model model) {
        model.addAttribute("cycles", cyclesRepository.findAll());
        return "display";
    }
}

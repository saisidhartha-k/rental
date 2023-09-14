package com.cycle.rental.service;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cycle.rental.entity.Cycles;
import com.cycle.rental.exception.CycleShopBusinessException;
import com.cycle.rental.repository.CyclesRepository;


@Service
public class CycleService {
    @Autowired
    private CyclesRepository cycleRepository;

    public List<Cycles> listCycles() {
        var listFromDB = cycleRepository.findAll();
        var cycleList = new ArrayList<Cycles>();
        listFromDB.forEach(cycleList::add);
        return cycleList;
    }

    public List<Cycles> listAvailableCycles() {
        return listCycles()
        .stream()
        .filter(cycle -> cycle.getNumAvailable()> 0)
        .collect(Collectors.toList());
    }

    public Cycles findByIdOrThrow404(long id) {
        var optCycle = cycleRepository.findById((int) id);
        if (optCycle.isEmpty()) {
            throw new CycleShopBusinessException(
                String.format("Can't find the cycle with id %d in the DB",
                id));
        }
        return optCycle.get();
    }

    public void borrowCycle(long id, int count) {
        var cycle = findByIdOrThrow404(id);
        cycle.setNumBorrowed(cycle.getNumBorrowed() + count);
        cycleRepository.save(cycle);
    }

    public void returnCycle(long id, int count) {
        var cycle = findByIdOrThrow404(id);
        cycle.setNumBorrowed(cycle.getNumBorrowed() - count);
        cycleRepository.save(cycle);
    }

    public void borrowCycle(long id) {
        borrowCycle(id, 1);
    }

    public void returnCycle(long id) {
        returnCycle(id, 1);
    }

    public void restockBy(long id, int count) {
        var cycle = findByIdOrThrow404(id);
        cycle.setStock(cycle.getStock() + count);
        cycleRepository.save(cycle);
    }

}
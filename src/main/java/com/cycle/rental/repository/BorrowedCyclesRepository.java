package com.cycle.rental.repository;

import com.cycle.rental.entity.BorrowedCycles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowedCyclesRepository extends JpaRepository<BorrowedCycles, Integer> {
}

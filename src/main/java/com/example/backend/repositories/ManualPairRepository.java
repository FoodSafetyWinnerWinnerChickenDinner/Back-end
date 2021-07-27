package com.example.backend.repositories;

import com.example.backend.models.ManualPairs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManualPairRepository extends JpaRepository<ManualPairs, Long> {
}

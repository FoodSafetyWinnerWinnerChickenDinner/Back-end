package com.example.backend.repositories;

import com.example.backend.models.ManualPairs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManualPairRepository extends CrudRepository<ManualPairs, Long> {
}

package com.example.backend.repositories;

import com.example.backend.models.Foods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOpenApiRepository extends CrudRepository<Foods, Long> {
}

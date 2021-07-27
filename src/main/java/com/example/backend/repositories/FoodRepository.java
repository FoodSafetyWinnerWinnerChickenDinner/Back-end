package com.example.backend.repositories;

import com.example.backend.models.Foods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Foods, Long> {
}

package com.example.backend.repositories;

import com.example.backend.models.Foods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Foods, Long> {
    @Query("SELECT f from Foods as f WHERE f.foodName = ?1 AND f.category = ?2 AND f.total = ?3")
    public Foods findByNameAndCategory(String name, String category, double total);

    @Query("SELECT COUNT(*) FROM Foods")
    public Long countAllData();
}

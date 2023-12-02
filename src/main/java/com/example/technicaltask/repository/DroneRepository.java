package com.example.technicaltask.repository;

import com.example.technicaltask.model.Drone;
import com.example.technicaltask.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    @Query(value = "select d from Drone as d where d.state = :state")
    Page<Drone> findAllAvailableDrones(Pageable pageable, State state);
}
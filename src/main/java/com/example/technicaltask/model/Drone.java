package com.example.technicaltask.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "drone")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 100)
    private String serialNumber;

    @Enumerated(EnumType.ORDINAL) // Enum values will be persisted as their ordinal values
    @Column(nullable = false)
    private Model model;

    @Column(nullable = false)
    private Integer weightLimit;


    @Column(nullable = false)
    private Integer batteryCapacity;

    @Enumerated(EnumType.ORDINAL) // Enum values will be persisted as their ordinal values
    @Column(nullable = false)
    private State state;

    @ManyToMany() // Specifies a many-to-many relationship with Medication entity
    @JoinTable(name = "drone_medication", joinColumns = @JoinColumn(name = "drone_serial_number"), inverseJoinColumns = @JoinColumn(name = "medication_code"))
    private List<Medication> loadedMedications;

    @PrePersist // Specifies a method to be executed before entity insertion
    public void onCreate() {
        state = State.IDLE; // Sets the default state to IDLE before insertion
    }

}
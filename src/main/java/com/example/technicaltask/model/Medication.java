package com.example.technicaltask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medication")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id
    private String code;

    @NotBlank
    private String name;

    private int weight;

    @Lob // Indicates that the field is a large object (LOB) in the database
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    private String imageUUID;

    @ManyToMany() // Specifies a many-to-many relationship with Drone entity
    @JoinTable(name = "drone_medication", joinColumns = @JoinColumn(name = "drone_serial_number"), inverseJoinColumns = @JoinColumn(name = "medication_code"))
    private List<Drone> drones;

    @PrePersist // Specifies a method to be executed before entity insertion
    public void generateCode() {
        // Generates a unique code for the medication using UUID and substring operations
        this.code = "MED_CODE_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
    }

}

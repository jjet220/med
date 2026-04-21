package com.medical.med.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String surname;

    private String name;

    private String patronymic;

    private LocalDate dateOfBirth;

    private SexType sex;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

}


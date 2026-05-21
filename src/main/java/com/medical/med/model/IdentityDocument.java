package com.medical.med.model;

import com.medical.med.model.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "identity_documents")
public class IdentityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DocumentType type;

    private String series;

    private String number;

    private LocalDate issuedAt;

    private String issuedBy;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

}

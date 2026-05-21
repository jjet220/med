package com.medical.med.model;

import com.medical.med.model.enums.AttachmentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @Column(name = "date_begin")
    private LocalDate dateOfBegin;

    @Column(name = "date_end")
    private LocalDate dateOfEnd;

    @OneToOne
    @JoinColumn(name = "mo_code", referencedColumnName = "code")
    private MedicalOrganization medicalOrganization;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}

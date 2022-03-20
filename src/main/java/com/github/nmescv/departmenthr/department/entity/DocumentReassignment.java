package com.github.nmescv.departmenthr.department.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Документ на перевод
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "document_reassignment")
public class DocumentReassignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boss_id")
    private Employee boss;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private Employee hr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_status_id")
    private DocumentStatus documentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "date_of_reassignment")
    private Date reassignmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_position")
    private Position newPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_department")
    private Department newDepartment;

    @Column(name = "new_salary")
    private Double salary;

    @Column(name = "is_approved")
    private Boolean isApproved = null;
}

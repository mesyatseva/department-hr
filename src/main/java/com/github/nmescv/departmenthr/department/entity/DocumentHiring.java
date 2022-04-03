package com.github.nmescv.departmenthr.department.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Документ на увольнение
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "document_hiring")
public class DocumentHiring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "boss_id")
    private Employee boss;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private Employee hr;

    @ManyToOne
    @JoinColumn(name = "document_status_id")
    private DocumentStatus documentStatus;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "date_of_hiring")
    private Date hiringDate;

    @Column(name = "new_salary")
    private Double salary;

    @Column(name = "is_approved")
    private Boolean isApproved = null;
}

package com.github.nmescv.departmenthr.department.entity;

import com.github.nmescv.departmenthr.security.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "boss_id", referencedColumnName = "id", unique = true)
    private Employee boss;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "department_position",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private List<Position> positions = new ArrayList<>();
}

package com.github.nmescv.departmenthr.department.dao;

import com.github.nmescv.departmenthr.department.repository.DocumentHiringRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DaoDocumentHiring {

    private final DocumentHiringRepository documentHiringRepository;

    public DaoDocumentHiring(DocumentHiringRepository documentHiringRepository) {
        this.documentHiringRepository = documentHiringRepository;
    }
}

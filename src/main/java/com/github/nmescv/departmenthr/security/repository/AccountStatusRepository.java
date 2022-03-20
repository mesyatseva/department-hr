package com.github.nmescv.departmenthr.security.repository;

import com.github.nmescv.departmenthr.security.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatusRepository extends JpaRepository<AccountStatus, Long> {

    AccountStatus findByName(String name);
}

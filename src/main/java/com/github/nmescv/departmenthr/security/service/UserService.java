package com.github.nmescv.departmenthr.security.service;

import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.security.account.Account;
import com.github.nmescv.departmenthr.security.dto.RegistrationForm;
import com.github.nmescv.departmenthr.security.entity.Role;
import com.github.nmescv.departmenthr.security.entity.User;
import com.github.nmescv.departmenthr.security.repository.AccountStatusRepository;
import com.github.nmescv.departmenthr.security.repository.RoleRepository;
import com.github.nmescv.departmenthr.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("userDetailsServiceImpl")
public class UserService implements UserDetailsService {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final AccountStatusRepository accountStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(AccountStatusRepository accountStatusRepository,
                       EmployeeRepository employeeRepository,
                       UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.accountStatusRepository = accountStatusRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String tabelName) throws UsernameNotFoundException {
        log.info(tabelName);
        Employee employee = employeeRepository.findByTabelNumber(tabelName);
        User user = userRepository.findByEmployee(employee);
        if (user == null)
            throw new RuntimeException("Нет такого пользователя");
        return Account.fromUser(user);
    }

    public void registerNewUser(RegistrationForm data) {

        Employee employee = employeeRepository.findById(data.getEmployeeId()).orElse(null);
        User user = userRepository.findByEmployee(employee);
        if (user != null)
            throw new RuntimeException("Введенный вами логин уже существует");
        if (!data.getConfirmedPassword().equals(data.getPassword()))
            throw new RuntimeException("Пароли не совпадают!");

        Role userRole = roleRepository.findByName(data.getRole());
        List<Role> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);

        User entity = new User();
        entity.setEmployee(employee);
        entity.setPassword(passwordEncoder.encode(data.getPassword()));
        entity.setStatus(accountStatusRepository.findByName(data.getAccountStatus()));
        entity.setCreatedDate(new Date());
        entity.setRoles(userRoleList);

        userRepository.save(entity);
    }
}

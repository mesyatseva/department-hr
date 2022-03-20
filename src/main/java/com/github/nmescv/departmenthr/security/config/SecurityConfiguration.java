package com.github.nmescv.departmenthr.security.config;

import com.github.nmescv.departmenthr.security.dict.Permissions;
import com.github.nmescv.departmenthr.security.dict.RoleDictionary;
import com.github.nmescv.departmenthr.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler;
import org.springframework.security.web.firewall.RequestRejectedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String hr = RoleDictionary.HR.getRole();
    private final String boss = RoleDictionary.BOSS.getRole();
    private final String employee = RoleDictionary.EMPLOYEE.getRole();

    @Autowired
    @Lazy
    @Qualifier("userDetailsServiceImpl")
    private UserService userService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(userService.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RequestRejectedHandler requestRejectedHandler() {
        return new HttpStatusRequestRejectedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                .antMatchers("/static/**", "/images/**", "/js/**", "/css/**", "/sass/**", "/img/**", "/pages/**", "/json/**").permitAll()

                .antMatchers(Permissions.LOGIN, "/page", "/todo").permitAll()

                .antMatchers(HttpMethod.GET, Permissions.ALL_EMPLOYEES, Permissions.PROFILE).hasAnyAuthority(employee, boss, hr)

                .antMatchers().hasAuthority(boss)

                .antMatchers(HttpMethod.GET, Permissions.CREATE_FORM_EMPLOYEE).hasAuthority(hr)
                .antMatchers(HttpMethod.POST, Permissions.REGISTER_NEW_WORKER).hasAuthority(hr)


                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/layout", true)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                .logoutSuccessUrl("/auth/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/auth/login");
    }


}

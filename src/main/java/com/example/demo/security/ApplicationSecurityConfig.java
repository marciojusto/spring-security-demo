package com.example.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.demo.security.ApplicationUserRole.ADMIN;
import static com.example.demo.security.ApplicationUserRole.ADMIN_TRAINEE;
import static com.example.demo.security.ApplicationUserRole.STUDENT;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                /*.antMatchers(DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())*/
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmith = User.builder()
                                    .username("annasmith")
                                    .password(passwordEncoder.encode("password"))
                                    //.roles(STUDENT.name())
                                    .authorities(STUDENT.getGrantedAuthorities())
                                    .build();

        UserDetails lindaUser = User.builder()
                                    .username("linda")
                                    .password(passwordEncoder.encode("password123"))
                                    //.roles(ADMIN.name())
                                    .authorities(ADMIN.getGrantedAuthorities())
                                    .build();

        UserDetails tomUser = User.builder()
                                  .username("tom")
                                  .password(passwordEncoder.encode("password123"))
                                  //.roles(ADMIN_TRAINEE.name())
                                  .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
                                  .build();

        return new InMemoryUserDetailsManager(
                annaSmith,
                lindaUser,
                tomUser
        );
    }
}

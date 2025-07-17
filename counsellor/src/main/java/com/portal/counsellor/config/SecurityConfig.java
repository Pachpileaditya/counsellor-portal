package com.portal.counsellor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.portal.counsellor.service.CounsellorUserdetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CounsellorUserdetailsService counsellorUserdetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(counsellorUserdetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Optional for manual login forms
            .authenticationProvider(authProvider())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/register", "/rpage", "/login", "/css/**", "/js/**", "/favicon.ico", "/dark-theme.css", "/static/**").permitAll()
                .requestMatchers("/dashboard", "/enquiry", "/viewEnquiry", "/addEnq", "/logout", "/filter-enquiry", "/view-enquiry").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/dashboard").hasRole("ADMIN")
                .anyRequest().denyAll()
            )

            .formLogin(form -> form
                .disable() // Very important - disable Spring Security form login
            )

            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}

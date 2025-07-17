package com.portal.counsellor.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.portal.counsellor.entity.Counsellor;
import com.portal.counsellor.repo.CounsellorRepo;


@Service
public class CounsellorUserdetailsService implements UserDetailsService
{


    @Autowired
    private CounsellorRepo counsellorRepo;

    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Counsellor user = counsellorRepo.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
    
}

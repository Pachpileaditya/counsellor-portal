package com.portal.counsellor.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portal.counsellor.DTO.DashboardResponse;
import com.portal.counsellor.entity.Counsellor;
import com.portal.counsellor.entity.Enquiry;
import com.portal.counsellor.repo.CounsellorRepo;
import com.portal.counsellor.repo.EnquiryRepo;

@Service
public class CounsellorServiceImpl implements CounsellorService {

    private CounsellorRepo counsellorRepo;

    private EnquiryRepo enquiryRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

  

    public CounsellorServiceImpl(CounsellorRepo counsellorRepo, EnquiryRepo enquiryRepo) {
        this.counsellorRepo = counsellorRepo;
        this.enquiryRepo = enquiryRepo;
    }

    @Override
    public boolean registerCounsellor(Counsellor c) {
        // Validate required fields
        if (c.getName() == null || c.getName().trim().isEmpty() ||
            c.getEmail() == null || c.getEmail().trim().isEmpty() ||
            c.getPassword() == null || c.getPassword().trim().isEmpty() ||
            c.getPhoneNo() == null) {
            return false;
        }
        // Check for duplicate email
        if (counsellorRepo.existsByEmail(c.getEmail())) {
            return false;
        }
        // Set default role if not set
        if (c.getRole() == null || c.getRole().trim().isEmpty()) {
            c.setRole("USER");
        }
        String encode = passwordEncoder.encode(c.getPassword());
        c.setPassword(encode);
        Counsellor saveCounsellor = counsellorRepo.save(c);
        return saveCounsellor.getId() != null;
    }

    @Override
    public Counsellor loginCounsellor(String email, String password) {
        Optional<Counsellor> optional = counsellorRepo.findByEmail(email);
        if (optional.isPresent()) {
            Counsellor c = optional.get();
            // Match raw password with encrypted one in DB

            System.out.println("Entered password: " + password);
            System.out.println("DB password: " + c.getPassword());
            System.out.println("Match: " + passwordEncoder.matches(password, c.getPassword()));
            if (passwordEncoder.matches(password, c.getPassword())) {
                System.out.println("2");
                return c;
            }
        }
        return null;
    }

    @Override
    public DashboardResponse getDashboardResponse(int counsellorId) {

        // EnquiryRepo

        List<Enquiry> enquiries = enquiryRepo.getAllEnquiresByCounsellorId(counsellorId);

        int totalEnqs = enquiries.size();
        int enrollEnqs = enquiries.stream()
                .filter(e -> e.getStatus().equals("Enrolled"))
                .collect(Collectors.toList())
                .size();
        int lostEnqs = enquiries.stream()
                .filter(e -> e.getStatus().equals("Lost"))
                .collect(Collectors.toList())
                .size();
        int operEnqs = enquiries.stream()
                .filter(e -> e.getStatus().equals("Open"))
                .collect(Collectors.toList())
                .size();

        DashboardResponse d = new DashboardResponse();
        d.setEnrollEnquiries(enrollEnqs);
        d.setLostEnquiries(lostEnqs);
        d.setOpenEnquiries(operEnqs);
        d.setTotalEnquiries(totalEnqs);

        return d;
    }

    @Override
    public Optional<Counsellor> findByEmail(String email) {
        return counsellorRepo.findByEmail(email);
    }

}

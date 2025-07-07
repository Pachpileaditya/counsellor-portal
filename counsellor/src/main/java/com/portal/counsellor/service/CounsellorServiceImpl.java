package com.portal.counsellor.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public CounsellorServiceImpl(CounsellorRepo counsellorRepo, EnquiryRepo enquiryRepo) {
        this.counsellorRepo = counsellorRepo;
        this.enquiryRepo = enquiryRepo;
    }

    @Override
    public boolean registerCounsellor(Counsellor c) {
       
        // do counsellor checking login in controller

        Counsellor saveCounsellor = counsellorRepo.save(c);
        if(null!=saveCounsellor.getId())
        {
            return true;
        }
        return false;

    }

    @Override
    public Counsellor loginCounsellor(String email, String password) 
    {
        return counsellorRepo.findByEmailAndPassword(email, password).orElse(null);
    }

    @Override
    public DashboardResponse getDashboardResponse(int counsellorId) {
        

        // EnquiryRepo

        List<Enquiry> enquiries = enquiryRepo.getAllEnquiresByCounsellorId(counsellorId);

        int totalEnqs = enquiries.size();
        int enrollEnqs = enquiries.stream()
                                .filter(e->e.getStatus().equals("Enrolled"))
                                .collect(Collectors.toList())
                                .size();
        int lostEnqs = enquiries.stream()
                                .filter(e->e.getStatus().equals("Lost"))
                                .collect(Collectors.toList())
                                .size();        
        int operEnqs = enquiries.stream()
                                .filter(e->e.getStatus().equals("Open"))
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

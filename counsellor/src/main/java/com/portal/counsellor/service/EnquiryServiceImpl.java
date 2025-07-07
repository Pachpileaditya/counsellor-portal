package com.portal.counsellor.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.portal.counsellor.DTO.ViewEnquiryWithFlterRequest;
import com.portal.counsellor.entity.Counsellor;
import com.portal.counsellor.entity.Enquiry;
import com.portal.counsellor.repo.CounsellorRepo;
import com.portal.counsellor.repo.EnquiryRepo;

import io.micrometer.common.util.StringUtils;


@Service
public class EnquiryServiceImpl implements EnquiryService {

    private EnquiryRepo enquiryRepo;
    private CounsellorRepo counsellorRepo;

    public EnquiryServiceImpl(EnquiryRepo enquiryRepo, CounsellorRepo counsellorRepo) {
        this.enquiryRepo = enquiryRepo;
        this.counsellorRepo = counsellorRepo;
    }

    @Override
    public boolean addEnquiry(Enquiry enquiry, Integer counsellorId) throws Exception 
    {

        Counsellor c = counsellorRepo.findById(counsellorId).orElse(null);

        if(c==null)
        {
            throw new Exception("Counsellor not found.");

        }

        enquiry.setCounsellor(c);

        Enquiry e = enquiryRepo.save(enquiry);

        if(e.getId()!=null)
        {
            return true;
        }
        return false;

    }

    @Override
    public List<Enquiry> getAllEnquiries(Integer counsellorId) {

        return enquiryRepo.getAllEnquiresByCounsellorId(counsellorId);
    }

    @Override
    public Enquiry getEnquiryById(int enquiryId) {
        return enquiryRepo.findById(enquiryId).orElse(null);
    }

    @Override
    public List<Enquiry> getEnquiriesWithFilter(ViewEnquiryWithFlterRequest v, Integer counsellorId) 
    {

        // QBE implementation (Dynamic Query preparation)

        Enquiry enq = new Enquiry();

        if(StringUtils.isNotEmpty(v.getClassMode())){
            enq.setClassMode(v.getClassMode());
        }

        if(StringUtils.isNotEmpty(v.getCourse()))
        {
            enq.setCourse(v.getCourse());
        }

        if(StringUtils.isNotEmpty(v.getStatus()))
        {
            enq.setStatus(v.getStatus());
        }

        Counsellor c = counsellorRepo.findById(counsellorId).orElse(null);
        enq.setCounsellor(c);

        Example<Enquiry> of = Example.of(enq);

        List<Enquiry> enqList = enquiryRepo.findAll(of);

        return enqList;
        
    }



}

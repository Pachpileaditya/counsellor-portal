package com.portal.counsellor.service;

import java.util.List;

import com.portal.counsellor.DTO.ViewEnquiryWithFlterRequest;
import com.portal.counsellor.entity.Enquiry;

public interface EnquiryService
{

    public boolean addEnquiry(Enquiry enquiry, Integer counsellorId) throws Exception;

    List<Enquiry> getAllEnquiries(Integer counsellorId);

    Enquiry getEnquiryById(int enquiryId);

    public List<Enquiry> getEnquiriesWithFilter(ViewEnquiryWithFlterRequest v, Integer counsellorId);

}

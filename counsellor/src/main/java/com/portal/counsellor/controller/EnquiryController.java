package com.portal.counsellor.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.portal.counsellor.DTO.ViewEnquiryWithFlterRequest;
import com.portal.counsellor.entity.Enquiry;
import com.portal.counsellor.service.EnquiryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class EnquiryController 
{
    private EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) 
    {
        this.enquiryService = enquiryService;
    }


    @GetMapping("/enquiry")
    public String addEnquiryPage(Model model)
    {
        Enquiry enqObj = new Enquiry();
        model.addAttribute("enq", enqObj);
        return "enquiry";
    }


    @GetMapping("/view-enquiry")
    public String updateEnquiry(@RequestParam("enqId") Integer id , Model model) 
    {

        Enquiry enquiryDB = enquiryService.getEnquiryById(id);

        model.addAttribute("enq", enquiryDB);
        
        return "enquiry";
    }


    @PostMapping("/addEnq")
    public String addEnquiry(@ModelAttribute("enq") Enquiry enquiry, HttpServletRequest request, Model model) throws Exception 
    {
        HttpSession session = request.getSession(false);
        Integer counsellorId = (Integer) session.getAttribute("counsellorId");
        boolean enq = enquiryService.addEnquiry(enquiry, counsellorId);
        // model.addAttribute("enq", new Enquiry());  -> @ModelAttribute
        // Caused by: java.Iang.IllegalStateException: Neither BindingResult nor plain target object for bean name 'enq' available as request attribute
        // use variable name enquiry then no need to set object in model attribute.
        if(enq!=true)
        {
            model.addAttribute("emsg", "Can't add Enquiry! Please try again.");
        }
        else{
            model.addAttribute("smsg", "Enquiry added successfully!");
        }

        enquiry = new Enquiry();
        model.addAttribute("enq", enquiry); // added empty enquiry object to display emmpty form on ui

        return "enquiry";
    }

    @GetMapping("/viewEnquiry")
	public String getViewEnquiries(HttpServletRequest request, Model model) 
	{
		HttpSession session = request.getSession(false);
		Integer id = (Integer) session.getAttribute("counsellorId"); 

        List<Enquiry> allEnquiries = enquiryService.getAllEnquiries(id);

        ViewEnquiryWithFlterRequest v = new ViewEnquiryWithFlterRequest();
        
        model.addAttribute("viewEnquiryWithFlterRequest", v);
		model.addAttribute("enquiries", allEnquiries);

		return "viewEnquiryPage";
	}
    
    @PostMapping("/filter-enquiry")
    public String getFilterEnquiries(ViewEnquiryWithFlterRequest viewEnquiryWithFlterRequest, HttpServletRequest request , Model model) 
    {

        HttpSession session = request.getSession(false);
        Integer id = (Integer) session.getAttribute("counsellorId");

        List<Enquiry> enquiriesWithFilter = enquiryService.getEnquiriesWithFilter(viewEnquiryWithFlterRequest, id);
        model.addAttribute("enquiries", enquiriesWithFilter);
        
        return "viewEnquiryPage";
    }
}
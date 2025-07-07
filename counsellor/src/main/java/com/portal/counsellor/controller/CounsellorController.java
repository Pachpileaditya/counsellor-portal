package com.portal.counsellor.controller;


import com.portal.counsellor.DTO.DashboardResponse;
import com.portal.counsellor.entity.Counsellor;
import com.portal.counsellor.service.CounsellorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class CounsellorController 
{

	private CounsellorService counsellorService;

	public CounsellorController(CounsellorService counsellorService) {
		this.counsellorService = counsellorService;
	}

	@GetMapping("/")	
	public String index(Model model) 
	{
		Counsellor c = new Counsellor();
		model.addAttribute("counsellor", c);
		
		return "index";
	}

	@PostMapping("/login")
	public String login(Counsellor counsellor, HttpServletRequest request, Model model) {
		
		Counsellor c = counsellorService.loginCounsellor(counsellor.getEmail(), counsellor.getPassword());
		
		if(c==null)
		{
			model.addAttribute("emsg", "Invalid Credentials! Please try again.");
			return "index";
		}
		else {
			// valid login
			HttpSession session = request.getSession(true);
			session.setAttribute("counsellorId", c.getId());

			DashboardResponse dbrespobj = counsellorService.getDashboardResponse(c.getId());
			model.addAttribute("dashboardInfo", dbrespobj);
			return "dashboardPage";
		}
	}

	@GetMapping("/rpage")
	public String registerView(Model model) 
	{

		Counsellor counsellor = new Counsellor();

		model.addAttribute("counsellor", counsellor);

		return "register";
	}

	@PostMapping("/register")
	public String register(Counsellor counsellor, Model model) 
	{

		boolean rc = counsellorService.registerCounsellor(counsellor);

		if(rc!=true)
		{
			model.addAttribute("emsg", "Could not register user!");
			return "register";
		}
		else
		{
			model.addAttribute("successmsg", "Registration Successful! Please Login.");
			return "index";
		}
	
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) 
	{

		HttpSession session = request.getSession(false);
		session.invalidate();

		return "redirect:/";
	}
	
	@GetMapping("/dashboard")
	public String getDashboard(HttpServletRequest request, Model model) 
	{

		HttpSession session = request.getSession(false);
		Integer id = (Integer) session.getAttribute("counsellorId");

		DashboardResponse dashboardResponse = counsellorService.getDashboardResponse(id);
		model.addAttribute("dashboardInfo", dashboardResponse);


		return "dashboardPage";
	}

}

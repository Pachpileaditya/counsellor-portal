package com.portal.counsellor.controller;

import com.portal.counsellor.DTO.DashboardResponse;
import com.portal.counsellor.entity.Counsellor;
import com.portal.counsellor.service.CounsellorService;
import com.portal.counsellor.repo.EnquiryRepo;
import com.portal.counsellor.entity.Enquiry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Controller
public class CounsellorController {

	private CounsellorService counsellorService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
    private AuthenticationManager authenticationManager;

    private EnquiryRepo enquiryRepo;

    @Autowired
    public CounsellorController(CounsellorService counsellorService, EnquiryRepo enquiryRepo) {
        this.counsellorService = counsellorService;
        this.enquiryRepo = enquiryRepo;
    }

	public CounsellorController(CounsellorService counsellorService) {
		this.counsellorService = counsellorService;
	}

	@GetMapping("/")
	public String index(Model model) {
		Counsellor c = new Counsellor();
		model.addAttribute("counsellor", c);

		return "index";
	}

	@PostMapping("/login")
	public String login(Counsellor counsellor, HttpServletRequest request, Model model) {

		// 1. Authenticate manually (your existing logic)
		Counsellor c = counsellorService.loginCounsellor(counsellor.getEmail(), counsellor.getPassword());

		if (c == null) {
			model.addAttribute("emsg", "Invalid Credentials! Please try again.");
			return "index";
		}

		// 2. Create Spring Security Authentication token
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(counsellor.getEmail(),
				counsellor.getPassword());

		try {
			// 3. Authenticate via AuthenticationManager
			Authentication auth = authenticationManager.authenticate(authToken);

			// 4. Set security context manually
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);

			// 5. Store security context in session
			HttpSession session = request.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

			// 6. Set your own custom session attribute
			session.setAttribute("counsellorId", c.getId());

			// 7. Check role and redirect accordingly
			boolean isAdmin = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> role.equals("ROLE_ADMIN"));
			if (isAdmin) {
				return "redirect:/admin/dashboard";
			}

			// 8. Load dashboard data
			DashboardResponse dashboardData = counsellorService.getDashboardResponse(c.getId());
			model.addAttribute("dashboardInfo", dashboardData);

			return "dashboardPage";

		} catch (AuthenticationException ex) {
			model.addAttribute("emsg", "Authentication failed. Please try again.");
			return "index";
		}
	}

	@GetMapping("/rpage")
	public String registerView(Model model) {

		Counsellor counsellor = new Counsellor();

		model.addAttribute("counsellor", counsellor);

		return "register";
	}

	@PostMapping("/register")
	public String register(Counsellor counsellor, Model model) {

		boolean registered = counsellorService.registerCounsellor(counsellor);

		if (!registered) {
			model.addAttribute("emsg", "Registration failed. Email may already exist.");
			return "register";
		} else {
			model.addAttribute("successmsg", "Registration successful. Please login.");
			return "index";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		session.invalidate();

		return "redirect:/";
	}

	@GetMapping("/dashboard")
	public String getDashboard(HttpServletRequest request, Model model) {

		HttpSession session = request.getSession(false);
		Integer id = (Integer) session.getAttribute("counsellorId");

		DashboardResponse dashboardResponse = counsellorService.getDashboardResponse(id);
		model.addAttribute("dashboardInfo", dashboardResponse);

		return "dashboardPage";
	}

	@GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        List<Enquiry> allEnquiries = enquiryRepo.findAllWithCounsellor();
        model.addAttribute("allEnquiries", allEnquiries);
        return "adminDashboard";
    }

}

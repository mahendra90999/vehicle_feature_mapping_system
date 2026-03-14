//package com.example.jpa.jpa1.Controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.jpa.jpa1.Dto.CredentialDto;
//import com.example.jpa.jpa1.Dto.StudentDto;
//import com.example.jpa.jpa1.Service.SignupService;
//import com.example.jpa.jpa1.Service.StudentService;
//import com.example.jpa.jpa1.Service.loginService;
//
//import org.springframework.ui.Model;
//
//
//
//@RestController
//@RequestMapping("/api")
//public class HomeController {
//	
//	@Autowired
//	private StudentService studentService;
//	
//	@Autowired
//	private loginService loginService;
//	
//	@Autowired
//	private SignupService signupService;
//	
//	@GetMapping("/getdata")
//	public List<StudentDto> getAllData() {
//		return studentService.getAllStudents();
//
//	}
//	
//	@PostMapping("/adddata")
//	public StudentDto setStudentData(@RequestBody StudentDto studentDto) {
//		return studentService.addStudent(studentDto);
//	}
//	
//	
////	@GetMapping("/login")
////	public String loginPage() {
////		return "login";
////	}
////	
////	@PostMapping("/login")
////	public String doLogin(@RequestBody CredentialDto credentialDto,Model model) {
////		
////		boolean success = loginService.login(credentialDto);
////		if(!success) {
////			model.addAttribute("error", "invalid username and password");			
////		}
////		
////		return success ? "home":"login";
////	}
//	
//	
////	@PostMapping("/signup")
////	public String dosignup(@RequestBody CredentialDto credentialDto,Model model) {
////		boolean b = false;
////		try {
////			signupService.saveuser(credentialDto);	
////			b = true;
////		}catch (Exception e) {
////			System.out.println(e);
////		}
////		
////		
////		if(!b) {
////			model.addAttribute("error", "invalid username and password");			
////		}
////		
////		
////		return b ? "credential saved":"credential not saved";
////	}
//}

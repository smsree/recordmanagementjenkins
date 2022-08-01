package com.axisbank.project2.controlller;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axisbank.project2.model.ERole;
import com.axisbank.project2.model.Role;
import com.axisbank.project2.model.User;
import com.axisbank.project2.repository.RoleRepository;
import com.axisbank.project2.repository.UserRepository;
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('AUDIT') or hasRole('ADMIN') or hasRole('OPERATIONAL')")
	public String userAccess() {
		return "User Content.";
	}
	@GetMapping("/operational")
	@PreAuthorize("hasRole('OPERATIONAL')")
	public String moderatorAccess() {
		return "OPERATIONAL Board.";
	}
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	@GetMapping("/audit")
	@PreAuthorize("hasRole('AUDIT')")
	public String auditAccess() {
		return "Audit board";
	}
	@PutMapping("/updateOtherUserRole")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateOtherUserRole(@RequestBody SignupRequest request) {
		System.out.println("hit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if(userRepository.existsByUsername(request.getUsername())) {
			
			 User updatedUser = userRepository.findByUsername(request.getUsername()).get();
			 Set<String> strRoles = request.getRoles();
			 Set<Role> roles= new HashSet<>();
			 strRoles.forEach(role -> {
					switch (role) {
					case "admin": 
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
						if(adminRole == null) {
							throw new RuntimeException("Error: Role is not found.");
						}
						roles.add(adminRole);
						break;
					case "audit":
						Role auditRole = roleRepository.findByName(ERole.ROLE_AUDIT).get();
						if(auditRole == null)
						{
							throw new RuntimeException("Error: Role is not found");
						}
						roles.add(auditRole);
						break;
					case "operational":
						Role operationRole  = roleRepository.findByName(ERole.ROLE_OPERATIONAL).get();
						if(operationRole == null) {
							throw new RuntimeException("Error : Role not found");
						}
						roles.add(operationRole);
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
					if(userRole == null) {
							throw new RuntimeException("Error:  role not found");
						}
						roles.add(userRole);
					}
				});
			 updatedUser.setRoles(roles);
			 userRepository.save(updatedUser);
			}
		else {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username not found for updation!"));
		}
		return ResponseEntity.ok(new MessageResponse("User updation successfully!"));
		}
	
	@GetMapping("/showAllUser")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> showAllUser(){
		return userRepository.findAll();
	}
}
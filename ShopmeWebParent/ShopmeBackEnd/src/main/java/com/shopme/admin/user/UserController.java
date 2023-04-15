package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1,model,"firstName","asc",null);
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable int pageNum,Model model,
			@Param("sortField") String sortField,@Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		Page<User>page=service.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> listUsers=page.getContent();
		
		long startCount=(pageNum-1)*UserService.USERS_PER_PAGE+1;
		long endCount=startCount +UserService.USERS_PER_PAGE-1;
		if(endCount > page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		
		String reverseSortDir= sortDir.equals("asc")?"desc":"asc";
		
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("keyword",keyword);
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("sortField",sortField);
		model.addAttribute("listUsers",listUsers);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role>listRepo=service.listRoles(); 
		User user=new User();
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRepo);
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		if(fileName.contains(" ")) {
		    fileName = fileName.replaceAll(" ", "_");
		}
		
		user.setPhotos(fileName);
		User savedUser=service.save(user);
		String uploadDir="user-photos/" + savedUser.getId();
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		redirectAttributes.addFlashAttribute("message","The user has been saved successfully");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id,Model model,
			RedirectAttributes redirectAttributes) {
		try {
		User user=service.get(id);
		List<Role>listRepo=service.listRoles(); 
		System.out.println(id);
		model.addAttribute("user",user);
		model.addAttribute("pageTitle","Edit|User {ID: " +id+ "}");
		model.addAttribute("listRoles",listRepo);
		return "user_form";
		}
		catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable int id,Model model
			,RedirectAttributes redirectAttributes) {
		try {
			service.delete(id); 
			redirectAttributes.addFlashAttribute("message","deleted record successfully");
			}
			catch(UserNotFoundException ex) {
				redirectAttributes.addFlashAttribute("message",ex.getMessage());
			}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable int id,
			@PathVariable ("status") boolean enabled,RedirectAttributes redirectAttributes) {
		service.generateStatus(id, enabled);
		String status=enabled ?"enabled":"disabled";
		String message="The user " + id+" has been " +status;
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/users";
	}
}

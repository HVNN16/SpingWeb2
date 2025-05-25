package donga.edu.demo.controllers;

import donga.edu.demo.models.User;
import donga.edu.demo.models.Company;
import donga.edu.demo.repository.UserRepository;
import donga.edu.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository; // Thêm dòng này

    // Hiển thị form tạo mới user
    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("companies", companyRepository.findAll()); // Truyền danh sách công ty
        return "create-user"; // Tên file Thymeleaf form thêm user
    }

    // Xử lý lưu user mới
    @PostMapping("/users")
    public String saveUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    // Hiển thị danh sách user
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "list-users";
    }

    // Hiển thị chi tiết user
    @GetMapping("/users/{id}")
    public String showUserDetail(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user-detail";
    }
}

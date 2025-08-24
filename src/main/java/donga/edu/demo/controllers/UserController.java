package donga.edu.demo.controllers;

import donga.edu.demo.models.User;
import donga.edu.demo.repository.UserRepository;
import donga.edu.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Danh sách user (Admin và User đều xem được)
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list-users";
    }

    // Form tạo mới user (Chỉ Admin)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("companies", companyRepository.findAll());
        return "user/create-user";
    }

    // Lưu user mới (Chỉ Admin)
    @PostMapping
    public String saveUser(@ModelAttribute User user) {
        // Mã hóa password trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/users";
    }

    // Form sửa user (Chỉ Admin)
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/users";
        model.addAttribute("user", user);
        model.addAttribute("companies", companyRepository.findAll());
        return "user/edit-user";
    }

    // Cập nhật user (Chỉ Admin)
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            existing.setRole(user.getRole());
            existing.setCompany(user.getCompany());
            if (!user.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(existing);
        }
        return "redirect:/users";
    }

    // Xóa user (Chỉ Admin)
    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    // Xem chi tiết user (Admin và User đều xem được)
    @GetMapping("/{id}")
    public String showUserDetail(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/users";
        model.addAttribute("user", user);
        return "user/user-detail";
    }
}

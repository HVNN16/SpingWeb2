package donga.edu.demo.controllers;

import donga.edu.demo.models.User;
import donga.edu.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Hiển thị form tạo mới user
    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
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


    @GetMapping("/users/{id}")
    public String showUserDetail(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/users"; // nếu không tìm thấy thì quay lại danh sách
        }
        model.addAttribute("user", user);
        return "user-detail"; // view hiển thị chi tiết user
    }
}

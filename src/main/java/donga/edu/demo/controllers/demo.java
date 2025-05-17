package donga.edu.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class demo {
    @GetMapping("/members")
    public String showMemberList(Model model) {
        List<String> members = Arrays.asList("Huỳnh Văn Nghĩa", "Nguyễn Thị Minh Nguyệt", "Nguyễn Trung Kỳ");
        model.addAttribute("members", members);
        return "member-list";
    }
}

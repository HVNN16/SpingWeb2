package donga.edu.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class demo {

    private final List<String> members = Arrays.asList(
            "Huỳnh Văn Nghĩa",
            "Nguyễn Thị Minh Nguyệt",
            "Nguyễn Trung Kỳ"
    );

    @GetMapping("/members")
    public String showMemberList(Model model) {
        model.addAttribute("members", members);
        return "member-list";
    }

    @GetMapping("/members/{name}")
    public String showMemberDetail(@PathVariable("name") String name, Model model) {
        model.addAttribute("memberName", name);
        return "member-detail";
    }
}

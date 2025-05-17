package donga.edu.demo.controllers;

import donga.edu.demo.models.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class demo {

    private final List<Member> members = List.of(
            new Member("Huỳnh Văn Nghĩaa", 25, "Leader", "nghia@donga.edu"),
            new Member("Nguyễn Thị Minh Nguyệt", 22, "Developer", "nguyet@donga.edu"),
            new Member("Nguyễn Trung Kỳ", 23, "Designer", "ky@donga.edu")
    );

    @GetMapping("/members")
    public String showMemberList(Model model) {
        model.addAttribute("members", members);
        return "member-list";
    }

    @GetMapping("/members/{name}")
    public String showMemberDetail(@PathVariable("name") String name, Model model) {
        Member memberDetail = members.stream()
                .filter(member -> member.getName().equals(name))
                .findFirst()
                .orElse(null);
        model.addAttribute("member", memberDetail);
        return "member-detail";
    }
}

package donga.edu.demo.models;

public class Member {
    private String name;
    private int age;
    private String role;
    private String email;

    public Member(String name, int age, String role, String email) {
        this.name = name;
        this.age = age;
        this.role = role;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}

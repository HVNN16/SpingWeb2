package donga.edu.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usertable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    private String username;  // thêm
    private String password;  // thêm
    private String role;      // thêm (nếu muốn phân quyền)

    // Liên kết đến Company (nhiều người dùng thuộc về 1 công ty)
    @ManyToOne
    @JoinColumn(name = "company_id") // tên cột khóa ngoại trong bảng usertable
    private Company company;

    public User() {}

    public User(String name, String email, String username, String password, String role) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter và Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}

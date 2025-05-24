//package donga.edu.demo.models;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "usertable")
//
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String email;
//
//    public User() {}
//
//    public User(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }
//
//    // Getter Setter
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//
//}

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

    // Liên kết đến Company (nhiều người dùng thuộc về 1 công ty)
    @ManyToOne
    @JoinColumn(name = "company_id") // tên cột khóa ngoại trong bảng usertable
    private Company company;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getter và Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}

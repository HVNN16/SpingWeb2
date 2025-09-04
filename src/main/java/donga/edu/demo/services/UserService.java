package donga.edu.demo.services;

import donga.edu.demo.repository.UserRepository;
import donga.edu.demo.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    // TODO: CRUD methods sẽ được Người B hoàn thiện
}

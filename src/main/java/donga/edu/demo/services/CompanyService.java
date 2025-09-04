package donga.edu.demo.services;

import donga.edu.demo.models.Company;
import donga.edu.demo.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // Create
    public Company create(Company company) {
        return companyRepository.save(company);
    }

    // Read all (paged)
    public Page<Company> list(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    // Read one
    public Company getById(Long id) {
        // Nếu không tìm thấy -> ném NoSuchElementException (500 Internal Server Error)
        return companyRepository.findById(id).orElseThrow();
    }

    // Update
    public Company update(Long id, Company companyData) {
        Company company = getById(id);
        company.setName(companyData.getName());
        company.setAddress(companyData.getAddress());
        return companyRepository.save(company);
    }

    // Delete
    public void delete(Long id) {
        Company company = getById(id);
        companyRepository.delete(company);
    }
}
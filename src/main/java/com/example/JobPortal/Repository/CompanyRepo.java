package com.example.JobPortal.Repository;

import com.example.JobPortal.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface CompanyRepo extends JpaRepository<Company,Long> {

}

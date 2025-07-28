package com.example.JobPortal.Repository;

import com.example.JobPortal.Model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job,Long> {
    @Query("SELECT j FROM Job j " +
            "WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(j.jobType) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(j.department) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR str(j.postedDate) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(j.company.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchJobs(@Param("keyword") String keyword);

}

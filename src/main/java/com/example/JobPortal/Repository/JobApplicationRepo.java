package com.example.JobPortal.Repository;

import com.example.JobPortal.Model.Job;
import com.example.JobPortal.Model.JobApplication;
import com.example.JobPortal.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication,Long> {
    List<JobApplication> findByUser(User user);
    Optional<JobApplication> findByUserAndJob(Optional<User> user,Optional<Job> job);
    long countByUserId(Long user_id);
}

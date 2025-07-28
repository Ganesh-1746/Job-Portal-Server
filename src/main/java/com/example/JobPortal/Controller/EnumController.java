package com.example.JobPortal.Controller;

import com.example.JobPortal.Model.Department;
import com.example.JobPortal.Model.JobType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class EnumController {

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartments() {
        List<String> departments = Arrays.stream(Department.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/jobtypes")
    public ResponseEntity<List<String>> getJobTypes() {
        List<String> jobTypes = Arrays.stream(JobType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(jobTypes);
    }
}


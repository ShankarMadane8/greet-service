package com.example.greetservice.repository;

import com.example.greetservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByStatus(String status);

    List<Student> findByStatus(String status, Pageable pageable);
}

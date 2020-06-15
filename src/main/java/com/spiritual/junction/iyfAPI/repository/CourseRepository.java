package com.spiritual.junction.iyfAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiritual.junction.iyfAPI.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}

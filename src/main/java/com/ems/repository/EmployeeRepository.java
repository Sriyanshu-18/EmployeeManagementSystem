package com.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	Optional<Employee> findByEmail(String email);
	List<Employee> findByDepartment(String department);
	
	List<Employee> findByStatus(String status);
	
	List<Employee> findByDesignation(String designation);
	
	List<Employee> findByDepartmentAndStatus(
	        String department,
	        String status);
	
	Page<Employee> findByDepartment(
	        String department,
	        Pageable pageable);
	
	Page<Employee> findByStatus(
	        String status,
	        Pageable pageable);
	
	Page<Employee> findByDepartmentAndStatus(
	        String department,
	        String status,
	        Pageable pageable);
}

package com.ems.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ems.dto.EmployeeDTO;
import com.ems.entity.Employee;

public interface EmployeeService {
	Employee createEmployee(Employee employee);
	
	List<EmployeeDTO> getAllEmployees();
	
	Optional<EmployeeDTO> getEmployeeById(Long id);
	
	Employee updateEmployee(Long id,Employee employee);
	
	void deleteEmployee(Long id);
	
	List<EmployeeDTO> getEmployeesByDepartment(String department);
	
	List<EmployeeDTO> getEmployeesByStatus(String status);
	
	List<EmployeeDTO> getEmployeesByDesignation(String designation);
	
	List<EmployeeDTO> getEmployeesByDepartmentAndStatus(
	        String department,
	        String status);
	
	List<EmployeeDTO> searchEmployees(String department, String status);
	
	Page<EmployeeDTO> getAllEmployees(Pageable pageable);
	
	Page<EmployeeDTO> searchEmployees(
	        String department,
	        String status,
	        int page,
	        int size,
	        String sortBy,
	        String direction
	);
	
	Page<EmployeeDTO> getEmployeesSorted(
	        int page,
	        int size,
	        String sortBy,
	        String direction
	);
}

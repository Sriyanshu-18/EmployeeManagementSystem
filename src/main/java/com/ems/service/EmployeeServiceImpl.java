package com.ems.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ems.dto.EmployeeDTO;
import com.ems.entity.Employee;
import com.ems.exception.EmployeeNotFoundException;
import com.ems.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }
    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
        		.stream()
        		.map(this::convertToDTO)
        		.toList();
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
        		.map(this::convertToDTO);
    }
    
    @Override
    public Employee updateEmployee(Long id,Employee employee){
    	
    	Employee existingEmployee=employeeRepository.findById(id)
    				.orElseThrow(() ->new EmployeeNotFoundException("Employee not found with id: " +id));
    	
    	existingEmployee.setEmployeeCode(employee.getEmployeeCode());
    	existingEmployee.setFirstName(employee.getFirstName());
    	existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhone(employee.getPhone());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setDesignation(employee.getDesignation());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setJoiningDate(employee.getJoiningDate());
        existingEmployee.setStatus(employee.getStatus());
        
        return employeeRepository.save(existingEmployee);
    	
    }
    @Override
    public void deleteEmployee(Long id) {
    	Employee existingEmployee =employeeRepository.findById(id)
    			.orElseThrow(()-> new EmployeeNotFoundException("Employee not found with id:" + id));
    		
    	employeeRepository.delete(existingEmployee);
    }
    
    private EmployeeDTO convertToDTO(Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setDesignation(employee.getDesignation());
        dto.setSalary(employee.getSalary());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setStatus(employee.getStatus());

        return dto;
    }

	@Override
	public List<EmployeeDTO> getEmployeesByDepartment(String department) {
		List<Employee> employees = employeeRepository.findByDepartment(department);
		
		return employees.stream().map(this::convertToDTO)
				.toList();
		
	}
	
	@Override
	public List<EmployeeDTO> getEmployeesByStatus(String status){
		List<Employee> employees = employeeRepository.findByStatus(status);
		
		return employees.stream().map(this::convertToDTO).toList();
		
	}

	@Override
	public List<EmployeeDTO> getEmployeesByDesignation(String designation) {
		List<Employee> employees = employeeRepository.findByDesignation(designation);
		
		return employees.stream().map(this::convertToDTO).toList();
	}
	
	@Override
	public List<EmployeeDTO> getEmployeesByDepartmentAndStatus(
	        String department,
	        String status) {

	    List<Employee> employees =
	            employeeRepository.findByDepartmentAndStatus(
	                    department,
	                    status);

	    return employees.stream()
	            .map(this::convertToDTO)
	            .toList();
	}
	
	@Override
	public List<EmployeeDTO> searchEmployees(
	        String department,
	        String status) {

	    List<Employee> employees;

	    if (department != null && status != null) {

	        employees = employeeRepository
	                .findByDepartmentAndStatus(department, status);

	    } else if (department != null) {

	        employees = employeeRepository
	                .findByDepartment(department);

	    } else if (status != null) {

	        employees = employeeRepository
	                .findByStatus(status);

	    } else {

	        employees = employeeRepository.findAll();
	    }

	    return employees.stream()
	            .map(this::convertToDTO)
	            .toList();
	}
	
	@Override
	public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {

	    Page<Employee> employees =
	            employeeRepository.findAll(pageable);

	    return employees.map(this::convertToDTO);
	}
	
	@Override
	public Page<EmployeeDTO> searchEmployees(
	        String department,
	        String status,
	        int page,
	        int size,
	        String sortBy,
	        String direction) {

	    Sort sort;

	    if (direction.equalsIgnoreCase("desc")) {

	        sort = Sort.by(sortBy).descending();

	    } else {

	        sort = Sort.by(sortBy).ascending();

	    }

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<Employee> employees;

	    // Department + Status
	    if (department != null && !department.isEmpty()
	            && status != null && !status.isEmpty()) {

	        employees = employeeRepository
	                .findByDepartmentAndStatus(
	                        department,
	                        status,
	                        pageable
	                );

	    }

	    // Department only
	    else if (department != null && !department.isEmpty()) {

	        employees = employeeRepository
	                .findByDepartment(
	                        department,
	                        pageable
	                );

	    }

	    // Status only
	    else if (status != null && !status.isEmpty()) {

	        employees = employeeRepository
	                .findByStatus(
	                        status,
	                        pageable
	                );

	    }

	    // No filters
	    else {

	        employees = employeeRepository
	                .findAll(pageable);

	    }

	    return employees.map(this::convertToDTO);
	}
	@Override
	public Page<EmployeeDTO> getEmployeesSorted(
	        int page,
	        int size,
	        String sortBy,
	        String direction) {

	    Sort sort;

	    if (direction.equalsIgnoreCase("desc")) {

	        sort = Sort.by(sortBy).descending();

	    } else {

	        sort = Sort.by(sortBy).ascending();

	    }

	    Pageable pageable =
	            PageRequest.of(page, size, sort);

	    return employeeRepository
	            .findAll(pageable)
	            .map(this::convertToDTO);
	}
}
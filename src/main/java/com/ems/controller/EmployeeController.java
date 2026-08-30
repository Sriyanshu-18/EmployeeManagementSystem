package com.ems.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.EmployeeDTO;
import com.ems.entity.Employee;
import com.ems.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	private final EmployeeService employeeService;
	
	 public EmployeeController(EmployeeService employeeService) {
	        this.employeeService = employeeService;
	 }
	 
	 @PostMapping
	 public Employee createEmployee(@Valid @RequestBody Employee employee) {

		 return employeeService.createEmployee(employee);
	 }
	 
	 @GetMapping
	 public List<EmployeeDTO> getAllEmployees() {
	     return employeeService.getAllEmployees();
	 }
	 
	 @GetMapping("/{id}")
	 public Optional<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
	     return employeeService.getEmployeeById(id);
	 }
	 
	 @PutMapping("/{id}")
	 public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		 return employeeService.updateEmployee(id,employee);
	 }
	 
	 @DeleteMapping("/{id}")
	 public void deleteEmployee(@PathVariable Long id) {
		 employeeService.deleteEmployee(id);
	 }
	 
	 @GetMapping("/department/{department}")
	 public List<EmployeeDTO> getEmployeesByDepartment(@PathVariable String department){
		 return employeeService.getEmployeesByDepartment(department);
	 }
	 
	 @GetMapping("/status/{status}")
	 public List<EmployeeDTO> getEmployeeByStatus(@PathVariable String status){
		 return employeeService.getEmployeesByStatus(status);
	 }
	 
	 @GetMapping("/designation/{designation}")
	 public List<EmployeeDTO> getEmployeesByDesignation(@PathVariable String designation){
		 return employeeService.getEmployeesByDesignation(designation);
	 }
	 
	 @GetMapping("/filter/{department}/{status}")
	 public List<EmployeeDTO> getEmployeesByDepartmentAndStatus(
	         @PathVariable String department,
	         @PathVariable String status) {

	     return employeeService.getEmployeesByDepartmentAndStatus(
	             department,
	             status);
	 }
	 
	/* @GetMapping("/search")
	 public List<EmployeeDTO> searchEmployees(
	         @RequestParam(required = false) String department,
	         @RequestParam(required = false) String status) {

	     return employeeService.searchEmployees(
	             department,
	             status);
	 }*/
	 
	 @GetMapping("/paged")
	 public Page<EmployeeDTO> getEmployeesWithPagination(
	         Pageable pageable) {

	     return employeeService.getAllEmployees(pageable);
	 }
	 
	 @GetMapping("/search")
	 public Page<EmployeeDTO> searchEmployees(

	         @RequestParam(required = false)
	         String department,

	         @RequestParam(required = false)
	         String status,

	         @RequestParam(defaultValue = "0")
	         int page,

	         @RequestParam(defaultValue = "10")
	         int size,

	         @RequestParam(defaultValue = "id")
	         String sortBy,

	         @RequestParam(defaultValue = "asc")
	         String direction) {

	     return employeeService.searchEmployees(
	             department,
	             status,
	             page,
	             size,
	             sortBy,
	             direction
	     );
	 }
	 @GetMapping("/sorted")
	 public Page<EmployeeDTO> getEmployeesSorted(

	         @RequestParam(defaultValue = "0") int page,

	         @RequestParam(defaultValue = "10") int size,

	         @RequestParam(defaultValue = "id") String sortBy,

	         @RequestParam(defaultValue = "asc") String direction) {

	     return employeeService.getEmployeesSorted(
	             page,
	             size,
	             sortBy,
	             direction
	     );
	 }
}

package com.zc.cris.mybatis.dao;

import com.zc.cris.mybatis.bean.Employee;

public interface EmployeeMapper {

	public Employee getById(Integer id);
	
	public boolean add(Employee employee);
	
	public Integer update(Employee employee);
	
	public boolean remove(Integer id);
}

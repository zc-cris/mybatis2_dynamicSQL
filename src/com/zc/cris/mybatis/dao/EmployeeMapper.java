package com.zc.cris.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.zc.cris.mybatis.bean.Employee;

public interface EmployeeMapper {

	public Employee findEmpByStep(Integer id);
	
	public Employee findEmpAndDeptById(Integer id);
	
	public Employee getEmpByIdReturnMyMap(Integer id);
	
	//多条记录封装成一个map，Map<String, Employee>:键是这条记录的列名，值是记录封装后的java对象
	// 告诉mybatis 封装的这个map使用哪列作为key（注意key的不可重复性）
	@MapKey(value="name")
	public Map<String, Employee> getEmpByNameLikeReturnMap(String name);
	
	public Map<String, Object> getEmpByIdReturnMap(Integer id);
	
	public List<Employee> getEmpsByNameLike(String name);
	
	public Employee getEmpByMap(Map<String, Object> map);
	
	public Employee getByIdAndName(@Param("id")Integer id, @Param("name")String name);
	
	public Employee getById(Integer id);
	
	public boolean add(Employee employee);
	
	public Integer update(Employee employee);
	
	public boolean remove(Integer id);
}

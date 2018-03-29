package com.zc.cris.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.zc.cris.mybatis.bean.Employee;

public interface EmployeeMapper {
	
	public List<Employee> findEmpsByRepeatedSql(Employee employee);
	
	public List<Employee> findEmpsByInnerParams(Employee employee);
	
	public boolean insertForeach(@Param("emps") List<Employee> emps);
	
	// 需要注意，如果使用 foreach 标签需要在形参上面标注 @Param 标签
	public List<Employee> findEmpsByConditionForeach(@Param("ids") List<Integer> ids);
	
	public void updateEmp(Employee employee);
	
	public List<Employee> findEmpsByConditionChoose(Employee employee);
	
	public List<Employee> findByConditionTrim(Employee employee);
	
	public List<Employee> findByConditionIf(Employee employee);
	
	
}

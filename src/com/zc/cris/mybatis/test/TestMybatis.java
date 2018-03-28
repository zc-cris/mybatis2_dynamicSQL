package com.zc.cris.mybatis.test;                                                                                                         
                                                                                                                                          
import java.io.IOException;                                                                                                               
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;                                                                                                                 
import java.util.List;                                                                                                                    
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import org.apache.ibatis.io.Resources;                                                                                                    
import org.apache.ibatis.session.SqlSession;                                                                                              
import org.apache.ibatis.session.SqlSessionFactory;                                                                                       
import org.apache.ibatis.session.SqlSessionFactoryBuilder;                                                                                
import org.junit.jupiter.api.Test;                                                                                                        
                                                                                                                                          
import com.zc.cris.mybatis.bean.Department;                                                                                               
import com.zc.cris.mybatis.bean.Employee;                                                                                                 
import com.zc.cris.mybatis.dao.DepartmentMapper;                                                                                          
import com.zc.cris.mybatis.dao.EmployeeMapper;                                                                                            
                                                                                                                                          
class TestMybatis{                                                                                                                       
                                                                                                                                          
	public SqlSessionFactory getSqlSessionFactory() throws IOException {                                                                  
		String resource = "mybatis-config.xml";                                                                                           
		InputStream inputStream = Resources.getResourceAsStream(resource);                                                                
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);                                          
		return sqlSessionFactory;                                                                                                         
	}                                                                                                                                     
	public SqlSession getSession() throws IOException {                                                                                   
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();                                                                     
		SqlSession session = sqlSessionFactory.openSession();                                                                             
		return session;                                                                                                                   
	}                                                                                                                                     
      
	/*
	 * 测试 foreach 标签进行 or 查询
	 */
	@Test
	void testForeach() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		
		ArrayList<Integer> ids = new ArrayList<>();
		List<Employee> list = mapper.findEmpsByConditionForeach(Arrays.asList(1,2,4,9));
		
		for (Employee employee : list) {
			System.out.println(employee);
		}
	}
	
	/*
	 * 测试set 标签和trim 标签完成数据update
	 */
	@Test
	void testSet() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		try {
			mapper.updateEmp(new Employee(1, "kobe", null, null));
			session.commit();
		} finally {
			session.close();
		}
	}
	
	/*
	 * 测试choose标签
	 */
	@Test
	void testChoose() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		List<Employee> emps = mapper.findEmpsByConditionChoose(new Employee(null, "",  null,""));
		for (Employee employee : emps) {
			System.out.println(employee);
		}
	}
	
	
	/*
	 * 测试trim标签
	 */
	@Test
	void testTrim() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		List<Employee> emps = mapper.findByConditionTrim(new Employee(null, "克%",  null,"cris@qq.com"));
		for (Employee employee : emps) {
			System.out.println(employee);
		}
		
	}
	
	/*
	 * 测试if和where标签
	 */
	@Test
	void testDynamicSqlIf() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee employee = new Employee(null, "克%",  null,"cris@qq.com");
		List<Employee> emps = mapper.findByConditionIf(employee);
		for (Employee employee2 : emps) {
			System.out.println(employee2);
		}
		
	}
	                                                                                                                                      
}                                                                                                                                         
                                                                                                                                          
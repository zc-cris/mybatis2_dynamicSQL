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
	 * 测试复用 sql 片段
	 */
	@Test
	void testRepeatedSql() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		
		List<Employee> list = mapper.findEmpsByRepeatedSql(new Employee(null, "%克%", null, null));
		for (Employee employee : list) {
			System.out.println(employee);
		}
		
	}
	
	
	/*
	 * 测试mybatis 自带的两个内置参数：_parameter 和 _databaseId
	 */
	@Test
	void testInnerParams() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		List<Employee> list = mapper.findEmpsByInnerParams(new Employee(null, "里", null, null));
		// 如果传入的参数是null，那么就不进行条件查询
//		List<Employee> list = mapper.findEmpsByInnerParams(null);
		for (Employee employee : list) {
			System.out.println(employee);
		}
	}
	
	
	/*
	 * 使用foreach 标签进行批量增加操作(CUD操作一定要最后提交session)
	 */
	@Test
	void testForeachInsert() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		try {
			List<Employee> emps = new ArrayList<>();
			emps.add(new Employee(null, "lilisi", '0', "lilisi@qq.com", new Department(1)));
			emps.add(new Employee(null, "库里", '1', "curry@qq.com", new Department(2)));
			boolean result = mapper.insertForeach(emps);
			session.commit();
			System.out.println(result);
		} finally {
			session.close();
		}
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
	 * 测试set 标签或者trim 标签完成数据update
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
	 * 测试choose标签进行分支判断查询
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
	 * 测试trim标签进行sql语句的切割
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
	 * 测试if和where标签进行多条件查询
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
                                                                                                                                          
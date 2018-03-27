package com.zc.cris.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.zc.cris.mybatis.bean.Employee;
import com.zc.cris.mybatis.dao.EmployeeMapper;

class TestMybatis {

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
	 * 测试使用级联属性或者 association标签（推荐）进行联合查询
	 * 实际开发中最常用的还是分步式联合查询
	 */
	@Test
	void testUionQuery() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
//		Employee emp = mapper.findEmpAndDeptById(1);
		Employee emp = mapper.findEmpByStep(1);
		//测试懒加载
		System.out.println(emp.getName());
		System.out.println(emp.getDepartment());
	}
	
	
	
	/*
	 * 测试自定的javaBean封装规则
	 */
	@Test
	void testMyResultMap() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee emp = mapper.getEmpByIdReturnMyMap(1);
		System.out.println(emp);
	}
	
	/*
	 * 测试返回值为map 类型（值为列名，值为封装的java对象）
	 */
	@Test
	void testResultMap2() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Map<String, Employee> map = mapper.getEmpByNameLikeReturnMap("克%");
		System.out.println(map);
	}
	
	/*
	 * 测试返回值为map 类型（key为列名，值为列值）
	 */
	@Test
	void testResultMap() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
		System.out.println(map);
	}
	
	/*
	 * 测试返回值为List类型，元素为pojo
	 */
	@Test
	void testResultList() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		
		List<Employee> list = mapper.getEmpsByNameLike("克%");
		for (Employee employee : list) {
			System.out.println(employee);
		}
	}
	
	/*
	 * 测试多个参数封装成map在mapper映射文件中的处理情况(推荐)
	 */
	@Test
	void testMap() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Map<String, Object> map = new HashMap<>();
		map.put("id", 2);
		map.put("name", "克里斯");
		map.put("table", "tb_employee");
		Employee emp = mapper.getEmpByMap(map);
		System.out.println(emp);
	}
	
	/*
	 * 测试多个参数在mapper映射文件中的处理情况（不推荐使用）
	 */
	@Test
	void testParameters() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee emp = mapper.getByIdAndName(2, "克里斯");
		System.out.println(emp);
		
	}
	
	/*
	 * 测试最基本的更新操作 RUD
	 * 1. mybatis 允许我们增删改操作直接定义以下类型返回值
	 * 		Boolean， Integer， Long
	 * 2. 手动提交 session
	 */
	@Test
	void testRUD() throws IOException {
		SqlSession session = getSession();
		try {
			Employee employee = new Employee(null, "克莉丝汀", '0', "cristing@qq.com");
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			// 测试新增
			@SuppressWarnings("unused")
			boolean result = mapper.add(employee);
			System.out.println(employee);
			
			//测试update
//			Employee employee = new Employee(1, "avirl", '0', "avirllavine@163.com");
//			Integer result = mapper.update(employee);
			
			//测试delete
//			boolean result = mapper.remove(3);
//			System.out.println(result);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	
	/*
	 * 测试 mybatis 的接口式编程
	 */
	@Test
	void testInterface() throws IOException {
		//1. 获取 SqlSessionFactory 对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//2. 获取SqlSession对象
		SqlSession session = sqlSessionFactory.openSession();
		//3. 获取接口的实现类对象（代理类型）
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		System.out.println(mapper.getClass().getName());
		//4. 直接调用代理的方法
		Employee emp = mapper.getById(1);
		System.out.println(emp);
		
	}
	
	
	/*
	 * 1. 根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象，这个配置文件有数据源等配置信息 2.
	 * sql映射文件：配置了每一个sql，以及sql的封装规则 3. 将sql映射文件注册到全局配置文件中 4. 写代码： -
	 * 根据全局配置文件得到SqlSessionFactory：用来创建SqlSession对象，使用其规定方法执行crud操作 -
	 * 一个SqlSession就是一次和数据库的会话，用完一定要关闭 - 需要使用sql的唯一标识（命名空间.sqlId）来告诉MyBatis
	 * 需要执行哪一个sql 语句
	 */
	@Test
	void test() throws IOException {
		SqlSession session = getSession();
		
		try {
			Employee employee = session.selectOne("com.zc.cris.mybatis.EmployeeMapper.getEmpById", 1);
			System.out.println(employee);
		} catch (Exception e) {
		} finally {
			session.close();
		}
	}
	
}

package com.zxg.ssmcurd.tests;

import com.zxg.ssmcurd.beans.Department;
import com.zxg.ssmcurd.beans.Employee;
import com.zxg.ssmcurd.dao.DepartmentMapper;
import com.zxg.ssmcurd.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by zxg on 2017/6/10.
 * 推荐Spring的项目就可以使用Spring的单元测试，可以自动注入我们需要的组件
 * 1、导入SpringTest模块
 * 2、@ContextConfiguration指定Spring配置文件的位置
 * 3、直接autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class EmployeeMapperTest {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 测试 员工的 curd
     */
    @Test
    public void testEmpAdd() {
        int selective = employeeMapper.insertSelective(new Employee("zxg", "1542885@qq.com", "0", 1));
        System.out.println("selective = " + selective);
    }

    @Autowired
    private SqlSession sqlSession;

    @Test
    public void testEmpBatchAdd() {
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i = 0; i < 100; i++) {
            mapper.insertSelective(new Employee(UUID.randomUUID().toString().substring(0, 6), UUID.randomUUID().toString().substring(7, 10) + "@qq.com", "0", 1));
        }
    }

    @Test
    public void testEmpBatchUpdate() {
        employeeMapper.updateByPrimaryKey(new Employee(101, "xxxxxxxx", "xxxx@qq.com", "0", 3));
    }

    @Test
    public void testSelectAll() {
        Employee employee = employeeMapper.selectByPrimaryKey(1);
        System.out.println("employee = " + employee);
    }

    @Test
    public void testDelete() {
        employeeMapper.deleteByPrimaryKey(1);
    }
}

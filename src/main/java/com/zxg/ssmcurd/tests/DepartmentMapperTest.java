package com.zxg.ssmcurd.tests;

import com.zxg.ssmcurd.beans.Department;
import com.zxg.ssmcurd.dao.DepartmentMapper;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zxg on 2017/6/10.
 * 推荐Spring的项目就可以使用Spring的单元测试，可以自动注入我们需要的组件
 *1、导入SpringTest模块
 *2、@ContextConfiguration指定Spring配置文件的位置
 *3、直接autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class DepartmentMapperTest {
    @Autowired
    private DepartmentMapper departmentMapper;
    /**
     * 先测试 DepartmentMapper
     */
    @Test
    public void testDeptAdd() {
        Department department = new Department();
//        department.setDeptName("开发部");
//        department.setDeptName("测试部");
        department.setDeptName("zzzzzz");
        departmentMapper.insertSelective(department);
    }

    @Test
    public void testSelectAll() {
        Department departments = departmentMapper.selectByPrimaryKey(4);
        System.out.println("departments = " + departments);
    }

    @Test
    public void testUpdate() {
        Integer flag = departmentMapper.updateByPrimaryKey(new Department(4, "xxxxx"));
        System.out.println("flag = " + flag);
    }

    @Test
    public void testDelete() {
        Integer flag = departmentMapper.deleteByPrimaryKey(4);
        System.out.println("flag = " + flag);
    }
}

package com.zxg.ssmcurd.tests;

import com.github.pagehelper.PageInfo;
import com.zxg.ssmcurd.beans.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zxg on 2017/6/10.
 * 使用 spring 测试模块提供的测试请求功能，测试 crud 的正确性
 * spring4 测试的时候，需要 Servlet3.0 的支持
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:springmvc.xml"})
public class EmployeeControllerTest {
    /**
     * 虚拟的 mvc 请求，获取处理结果
     */
    MockMvc mockMvc;
    /**
     * 传入 spring mvc 的 IOC 容器
     */
    @Autowired
    WebApplicationContext context;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetPage() {
        try {
            /**
             * 模拟请求，拿到请求值。
             */
            MvcResult pageNumber = this.mockMvc.perform(
                    MockMvcRequestBuilders.get("/emps")
                            .param("pageNumber", "4").param("pageSize", "5")).andReturn();

            /**
             * 请求成功以后，请求域中会有 pageInfo, 可以取出 pageInfo, 进行验证
             */
            PageInfo pageInfo = (PageInfo) pageNumber.getRequest().getAttribute("pageInfo");
            System.out.println("当前页面 = " + pageInfo.getPageNum());
            System.out.println("总页数：" + pageInfo.getPages());
            System.out.println("每页数据大小：" + pageInfo.getPageSize());

            int[] nums = pageInfo.getNavigatepageNums();
            Arrays.stream(nums).forEach(System.out::println);

            List<Employee> employeeList = pageInfo.getList();
            employeeList.stream().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

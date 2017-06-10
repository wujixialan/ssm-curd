package com.zxg.ssmcurd.controllers;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxg.ssmcurd.beans.Employee;
import com.zxg.ssmcurd.beans.Message;
import com.zxg.ssmcurd.services.EmployeeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zxg on 2017/6/10.
 *
 * 处理员工的增删改查
 */
@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    /**
     * emps：获取所有员工的 列表（list）
     * @return
     */
//    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                          Model model) {
        /**
         * 这不是一个分页查询；
         * 引入PageHelper分页插件
         * 在查询之前只需要调用，传入页码，以及每页的大小
         */
        PageHelper.startPage(pageNumber, pageSize);
        /**
         * startPage后面紧跟的这个查询就是一个分页查询
         */
        List<Employee> employeeList = employeeService.getAllEmp();

        /**
         * 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
         * 封装了详细的分页信息,包括有我们查询出来的数据，传入连续显示的页数
         */
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList, 5);
        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }

    @ResponseBody
    @RequestMapping("/emps")
    public Message getEmps1(@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        /**
         * 这不是一个分页查询；
         * 引入PageHelper分页插件
         * 在查询之前只需要调用，传入页码，以及每页的大小
         */
        PageHelper.startPage(pageNumber, pageSize);
        /**
         * startPage后面紧跟的这个查询就是一个分页查询
         */
        List<Employee> employeeList = employeeService.getAllEmp();

        /**
         * 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
         * 封装了详细的分页信息,包括有我们查询出来的数据，传入连续显示的页数
         */
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList, 5);
        return Message.success().add("pageInfo", pageInfo);
    }
}

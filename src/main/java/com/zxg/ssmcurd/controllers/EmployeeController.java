package com.zxg.ssmcurd.controllers;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxg.ssmcurd.beans.Employee;
import com.zxg.ssmcurd.beans.Message;
import com.zxg.ssmcurd.services.EmployeeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 2017/6/10.
 * <p>
 * 处理员工的增删改查
 */
@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * emps：获取所有员工的 列表（list）
     *
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

    /**
     * 1. 要支持 JSR303
     * 2. 需要导入 hibernate-validator
     * <p>
     * 员工的添加
     *
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Message addEmp(@Valid Employee employee, BindingResult bindingResult, @Param("dId") Integer dId) {
        if (bindingResult.hasErrors()) {
            /**
             * 校验失败，应该返回失败，在模态框中显示校验失败的信息
             */
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, Object> errorMap = new HashMap<>();
            fieldErrors.stream().forEach((error) -> {
                System.out.println("错误字段名：" + error.getField());
                System.out.println("错误信息：" + error.getDefaultMessage());
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return Message.fail().add("errorMap", errorMap);
        } else {
            employee.setdId(dId);
            System.out.println("employee = " + employee);
            employeeService.addEmp(employee);
            return Message.success();
        }
    }

    /**
     * 检验用户名是否可用
     *
     * @param empName
     * @return
     */
    @RequestMapping("/checkUser")
    @ResponseBody
    public Message checkEmpName(@RequestParam("empName") String empName) {
        /**
         * 判断用户名是否满足表达式
         */
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]+$){3,8}";
        boolean matches = empName.matches(regx);
        if (!matches) {
            return Message.fail().add("vaMsg", "用户名可以是3-8位中文或者是6-16位字母和数字的组合");
        }

        /**
         * 如果成功，才有必要进行数据库校验。
         */
        Boolean flag = employeeService.checkEmpName(empName);
        System.out.println(flag);
        System.out.println(empName);
        if (flag) {
            return Message.success();
        } else {
            return Message.fail().add("vaMsg", "用户名不可用");
        }
    }

    /**
     * 查询员工，为修改进行回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Message getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmp(id);
        return Message.success().add("emp", employee);
    }


    /**
	 * 如果直接发送ajax=PUT形式的请求
	 * 封装的数据
	 * Employee
	 * [empId=1014, empName=null, gender=null, email=null, dId=null]
	 *
	 * 问题：
	 * 请求体中有数据；
	 * 但是Employee对象封装不上；
	 * update tbl_emp  where emp_id = 1014;
	 *
	 * 原因：
	 * Tomcat：
	 * 		1、将请求体中的数据，封装一个map。
	 * 		2、request.getParameter("empName")就会从这个map中取值。
	 * 		3、SpringMVC封装POJO对象的时候。
	 * 				会把POJO中每个属性的值，request.getParamter("email");
	 * AJAX发送PUT请求引发的血案：
	 * 		PUT请求，请求体中的数据，request.getParameter("empName")拿不到
	 * 		Tomcat一看是PUT不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
	 * org.apache.catalina.connector.Request--parseParameters() (3111);
	 *
	 * protected String parseBodyMethods = "POST";
	 * if( !getConnector().isParseBodyMethod(getMethod()) ) {
                success = true;
                return;
            }
	 *
	 *
	 * 解决方案；
	 * 我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
	 * 1、配置上HttpPutFormContentFilter；
	 * 2、他的作用；将请求体中的数据解析包装成一个map。
	 * 3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
	 * 员工更新方法
	 * @param employee
	 * @return
	 */
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Message modify(Employee employee) {
        System.out.println("employee = " + employee);
        employeeService.modifyEmp(employee);
        return Message.success();
    }

    @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deleteEmpById(@PathVariable("id") String empIds) {
        System.out.println(empIds);
        String[] ids = empIds.split(",");
        Arrays.stream(ids).forEach((id) -> {
            employeeService.deleteEmpById(Integer.valueOf(id));
        });
        return Message.success();
    }
}

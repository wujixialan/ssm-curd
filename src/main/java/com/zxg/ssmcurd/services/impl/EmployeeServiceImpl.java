package com.zxg.ssmcurd.services.impl;

import com.zxg.ssmcurd.beans.Employee;
import com.zxg.ssmcurd.dao.EmployeeMapper;
import com.zxg.ssmcurd.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 2017/6/10.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmp() {
        return employeeMapper.selectByExampleWithDept(null);
    }
}

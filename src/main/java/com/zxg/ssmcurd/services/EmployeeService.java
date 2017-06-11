package com.zxg.ssmcurd.services;

import com.zxg.ssmcurd.beans.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 2017/6/10.
 */

public interface EmployeeService {
    List<Employee> getAllEmp();

    void addEmp(Employee employee);

    Boolean checkEmpName(String empName);
}

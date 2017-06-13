package com.zxg.ssmcurd.services.impl;

import com.zxg.ssmcurd.beans.Employee;
import com.zxg.ssmcurd.beans.EmployeeExample;
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

    @Override
    public void addEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    @Override
    public Boolean checkEmpName(String empName) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andEmpNameEqualTo(empName);
        long count = employeeMapper.countByExample(employeeExample);

        return count == 0;

    }

    @Override
    public Employee getEmp(Integer id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void modifyEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    @Override
    public void deleteEmpById(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }
}

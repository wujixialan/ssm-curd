package com.zxg.ssmcurd.beans;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

public class Employee {
    private Integer empId;

    @Pattern(regexp = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]+$){3,8}",
            message = "用户名必须是3-8位中文或者是6-16位字母和数字的组合")
    private String empName;

    @Pattern(regexp = "^[a-z\\d]+(\\.[a-z\\d]+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$",
            message = "邮箱格式不正确")
    private String empEmail;

    private String empGender;

    private Integer dId;

    private Department dept;

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", empGender='" + empGender + '\'' +
                ", dept=" + dept +
                '}';
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail == null ? null : empEmail.trim();
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender == null ? null : empGender.trim();
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }

    public Employee() {
    }

    public Employee(Integer empId, String empName, String empEmail, String empGender, Integer dId) {
        this.empId = empId;
        this.empName = empName;
        this.empEmail = empEmail;
        this.empGender = empGender;
        this.dId = dId;
    }

    public Employee(String empName, String empEmail, String empGender, Integer dId) {
        this.empName = empName;
        this.empEmail = empEmail;
        this.empGender = empGender;
        this.dId = dId;
    }

    public Employee(String empName, String empEmail, String empGender, Department dept) {
        this.empName = empName;
        this.empEmail = empEmail;
        this.empGender = empGender;
        this.dept = dept;
    }


}
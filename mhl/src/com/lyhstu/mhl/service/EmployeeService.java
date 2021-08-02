package com.lyhstu.mhl.service;

import com.lyhstu.mhl.dao.EmployeeDAO;
import com.lyhstu.mhl.domain.Employee;

//该类完成对employee表的各种操作(通过调用EmployeeDAO对象完成)
public class EmployeeService {
    //定义一个EmployeeDAO属性
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    //根据empId 和 pwd 返回一个Employee对象,如果查询不到返回null,根据返回结果是否为空判断用户名密码是否匹配
    public Employee getEmployeeByIdAndPwd(String id, String pwd) {
        String sql = "select * from employee where empId = ? and pwd=md5(?)";
        return employeeDAO.querySingle(sql, Employee.class, id, pwd);//返回Employee或者null
    }
}

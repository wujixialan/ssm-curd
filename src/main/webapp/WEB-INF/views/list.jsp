<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/10
  Time: 11:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/common.jsp" %>
<html>
<head>
    <%--
        web路径：
            不以/开始的相对路径，找资源，以当前资源的路径为基准，经常容易出问题。
            以/开始的相对路径，找资源，以服务器的路径为标准(http://localhost:3306)；需要加上项目名
                    http://localhost:3306/crud
    --%>
    <title>员工列表</title>
</head>
<body>

<%--
    搭建页面
--%>
<div class="container-fluid">
    <%--
        标题
    --%>
    <div class="row">
        <div class="col-md-12">
            <h1>SSM CRUD</h1>
        </div>
    </div>

    <%--
        按钮
    --%>
    <div class="row">
        <div class="col-md-4 col-md-offset-8">
            <button class="btn btn-primary">新增</button>
            <button class="btn btn-danger">删除</button>
        </div>
    </div>

    <%--
        显示数据表格
    --%>
    <div class="row">
        <table class="table table-bordered" style="text-align: center">
            <tr>
                <td>ID</td>
                <td>empName</td>
                <td>empEmail</td>
                <td>empGender</td>
                <td>部门</td>
                <td>操作</td>
            </tr>

            <c:forEach items="${pageInfo.list}" var="emp">
                <tr>
                    <td>${emp.empId}</td>
                    <td>${emp.empName}</td>
                    <td>${emp.empEmail}</td>
                    <td>${emp.empGender == "0"? "男" : "女"}</td>
                    <td>${emp.dept.deptName}</td>
                    <td>
                        <button class="btn btn-primary btn-sm">
                            <span class="glyphicon glyphicon-edit"></span>
                            编辑
                        </button>
                        <button class="btn btn-primary btn-sm">
                            <span class="glyphicon glyphicon-remove"></span>
                            删除
                        </button>
                    </td>
                </tr>
            </c:forEach>

        </table>
    </div>

    <%--
        显示分页信息
    --%>
    <div class="row">
        <div class="col-md-6">
            当前第 ${pageInfo.pageNum} 页，总共有 ${pageInfo.pages} 页，记录数：${pageInfo.total} 条数据。
        </div>
        <div class="col-md-6">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${pageInfo.isFirstPage}">
                        <li class="disabled">
                            <a href="/emps?pageNumber=${pageInfo.firstPage}&pageSize=10">首页</a>
                        </li>
                    </c:if>
                    <c:if test="${!pageInfo.isFirstPage}">
                        <li>
                            <a href="/emps?pageNumber=${pageInfo.firstPage}&pageSize=10">首页</a>
                        </li>
                        <li>
                            <a href="/emps?pageNumber=${pageInfo.prePage}&pageSize=10" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach items="${pageInfo.navigatepageNums}" var="pageNumber">
                        <c:if test="${pageNumber == pageInfo.pageNum}">
                            <li class="active">
                                <a href="/emps?pageNumber=${pageInfo.pageNum}&pageSize=10">${pageNumber}</a>
                            </li>
                        </c:if>
                        <c:if test="${pageNumber != pageInfo.pageNum}">
                            <li>
                                <a href="/emps?pageNumber=${pageNumber}&pageSize=10">${pageNumber}</a>
                            </li>
                        </c:if>
                    </c:forEach>
                    <c:if test="${pageInfo.isLastPage}">
                        <li class="disabled">
                            <a href="${pageInfo.lastPage}">末页</a>
                        </li>
                    </c:if>

                    <c:if test="${!pageInfo.isLastPage}">
                        <li>
                            <a href="/emps?pageNumber=${pageInfo.nextPage}&pageSize=10" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>

                        <li>
                            <a href="/emps?pageNumber=${pageInfo.lastPage}&pageSize=10">末页</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>


</body>
</html>

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
    <script language="JavaScript">
        <%--
            页面加载完成以后，直接发送 ajax 请求，要到分页数据
        --%>
        $(function () {
//            去到首页
            to_page(1);
        });

        function to_page(pageNumber) {
            $.ajax({
                url: "/emps",
                type: "post",
                data: {
                    pageNumber: pageNumber,
                    pageSize: 5
                },
                success: function (result) {
//                    1. 请求成功，解析 json，显示员工信息
                    build_emps_table(result);
//                    2.显示分页信息
                    build_page_info(result);
//                    3.显示分页条信息
                    build_page_nav(result);
                }
            });
        }

        function build_emps_table(result) {
            $("#tbody").empty();
            $.each(result.map.pageInfo.list, function (index, item) {
                var empId = $("<td></td>").append(item.empId);
                var empName = $("<td></td>").append(item.empName);
                var empEmail = $("<td></td>").append(item.empEmail);
                var empGender = $("<td></td>").append(item.empGender == 0 ? "男" : "女");
                var deptName = $("<td></td>").append(item.dept.deptName);
                var $editSpan = $("<span></span>").addClass("glyphicon glyphicon-edit");
                var editBtn = $("<button></button>").addClass("btn btn-primary btn-sm")
                    .append($editSpan).append("编辑");
                var $removeSpan = $("<span></span>").addClass("glyphicon glyphicon-remove");
                var removeBtn = $("<button></button>").addClass("btn btn-danger btn-sm")
                    .append($removeSpan).append("删除");
                var $td = $("<td></td>").append(editBtn).append("&nbsp;&nbsp;&nbsp;&nbsp;").append(removeBtn);
                $("<tr></tr>").append(empId)
                    .append(empName)
                    .append(empEmail)
                    .append(empGender)
                    .append(deptName)
                    .append($td)
                    .appendTo("#tbody");
            });
        }

        function build_page_info(result) {
            $("#page_info").empty();
            var pageInfo = result.map.pageInfo;
            $("#page_info").append("当前第 " + pageInfo.pageNum + " 页，" +
                "总共有 " + pageInfo.pages + " 页，总记录 " + pageInfo.total + " 条数据。");
        }

        function build_page_nav(result) {
            console.log(result.map.pageInfo);
            $("#page_nav").empty();
            var $ul = $("<ul></ul>").addClass("pagination");
            var pageInfo = result.map.pageInfo;
            var firstPageLi = $("<li></li>").append($("<a></a>").append("首页"));
            firstPageLi = pageInfo.isFirstPage ? firstPageLi.addClass("disabled") : firstPageLi.click(function () {
                to_page(1);
            });


            var prePageLi = $("<li></li>").append($("<a></a>").append(
                $("<span></span>").append("&laquo;").attr("aria-hidden", true)
            ).attr("aria-label", "Previous"));
            prePageLi = pageInfo.isFirstPage ? prePageLi.addClass("disabled") : prePageLi.click(function () {
                to_page(pageInfo.prePage);
            });

            $ul.append(firstPageLi)
                .append(prePageLi);
            var navigatepageLi = "";
            $.each(pageInfo.navigatepageNums, function (index, item) {
                console.log(item);
                navigatepageLi = $("<li></li>")
                    .append($("<a></a>").append(item));
                navigatepageLi = pageInfo.pageNum == item ? navigatepageLi.addClass("active") : navigatepageLi;
                navigatepageLi.click(function () {
                    console.log(item);
                    to_page(item);
                });
                $ul.append(navigatepageLi);
            });

            var nextPageLi = $("<li></li>").append($("<a></a>").append(
                $("<span></span>").append("&raquo;").attr("aria-hidden", true)
            ).attr("aria-label", "Next"));
            nextPageLi = pageInfo.isLastPage ? nextPageLi.addClass("disabled") : nextPageLi.click(function () {
                to_page(pageInfo.nextPage);
            });


            var lastPageLi = $("<li></li>").append($("<a></a>").append("末页"));
            lastPageLi = pageInfo.isLastPage ? lastPageLi.addClass("disabled") : lastPageLi.click(function () {
                to_page(pageInfo.lastPage);
            });
            lastPageLi.click(function () {
                to_page(pageInfo.lastPage);
            });
            $ul.append(navigatepageLi)
                .append(nextPageLi)
                .append(lastPageLi)
                .appendTo("#page_nav");

        }
    </script>
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
            <thead>
            <tr>
                <td>ID</td>
                <td>empName</td>
                <td>empEmail</td>
                <td>empGender</td>
                <td>部门</td>
                <td>操作</td>
            </tr>
            </thead>
            <tbody id="tbody"></tbody>

        </table>
    </div>

    <%--
        显示分页信息
    --%>
    <div class="row">
        <div class="col-md-6" id="page_info">

        </div>
        <div class="col-md-6">
            <nav aria-label="Page navigation" id="page_nav">

            </nav>
        </div>
    </div>
</div>


</body>
</html>

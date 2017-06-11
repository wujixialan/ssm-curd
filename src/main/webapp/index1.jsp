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
        var totalPage = 0;
        <%--
            页面加载完成以后，直接发送 ajax 请求，要到分页数据
        --%>
        $(function () {
//            去到首页
            to_page(1);

            /**
             * 点击新增，打开模态框
             */
            $("#emp_add_modal").click(function () {
                /**
                 *  重置表单
                 */
                reset_form("#emp_add form");
                $("#emp_add").modal({
                    backdrop: "static",
                    show: true
                });
            });

            /**
             * 请求所有部门，动态创建部门的下拉列表
             */
            getDept();

            /**
             * $("#emp_add form").serialize() 是对表单进行序列化
             */
            $("#add_emp_btn").click(function () {
                console.log($("#emp_add form").serialize());
                if (!validate_add_form()) {
                    return false;
                }

                /**
                 * 取出 add_emp_btn 的属性 ajax-status，判断上个 ajax 是否可用。
                 */
                if ($("#add_emp_btn").attr("ajax-status") === "success") {
                    $.ajax({
                        url: "/emp",
                        type: "post",
                        data: $("#emp_add form").serialize(),
                        success: function (result) {
                            console.log(result);
                            /**
                             * 1. 处理成功，关闭模态框
                             */
                            $("#emp_add").modal("hide");

                            /**
                             * 2. 去到最后一页
                             */
                            to_page(totalPage);
                        }
                    });
                }
            });


            $("#emp_name").change(function () {
                $.ajax({
                    url: "/checkUser",
                    type: "post",
                    data: {
                        empName: $("#emp_name").val()
                    },
                    success: function (result) {
                        if (result.code === 200) {
                            show_validate_msg("#emp_name", "success", "用户名可用");
                            $("#add_emp_btn").attr("ajax-status", "success");
                        } else if (result.code === 400) {
                            show_validate_msg("#emp_name", "error", result.map.vaMsg);
                            $("#add_emp_btn").attr("ajax-status", "error");
                        }
                    }
                });
            });
        });

        function reset_form(ele) {
            $(ele)[0].reset();
            $(ele).find("*").removeClass("has-error has-success");
            $(ele).find(".help-block").text("");
        }

        /**
         * 校验表单数据
         */
        function validate_add_form() {
            /**
             * 1. 拿到需要校验的数据
             */
            var empName = $("#emp_name").val();
            var regName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]+$){3,8}/;

            if (!regName.test(empName)) {
//                confirm("用户名可以是3-8位中文或者是6-16位字母和数字的组合");
//                $("#emp_name").parent().addClass("has-error");
//                $("#emp_name").next("span").text("用户名可以是3-8位中文或者是6-16位字母和数字的组合");

                show_validate_msg("#emp_name", "error", "用户名可以是3-8位中文或者是6-16位字母和数字的组合");
                return false;
            }
            else {
//                $("#emp_name").parent().removeClass("has-error").addClass("has-success");
//                $("#emp_name").next("span").text("用户名正确");
                show_validate_msg("#emp_name", "success", "用户名正确");
            }

            var empEmail = $("#emp_email").val();
//            todo 正则表达式不明白
            var regEmail = /^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/;
            console.log(empEmail);
            console.log(regEmail.test(empEmail));
            if (!regEmail.test(empEmail)) {
//                confirm("邮箱格式不正确");
//                $("#emp_email").parent().addClass("has-error");
//                $("#emp_email").next("span").text("邮箱格式不正确");
                show_validate_msg("#emp_email", "error", "邮箱格式不正确");
                return false;
            } else {
//                $("#emp_email").parent().removeClass("has-error").addClass("has-success");
//                $("#emp_email").next("span").text("邮箱正确");
                show_validate_msg("#emp_email", "success", "邮箱正确");
            }

            return true;
        }

        function show_validate_msg(ele, status, msg) {
            console.log(status);
            $(ele).parent().removeClass("has-error has-success");
            $(ele).next("span").text("");
            if ("error" === status) {
                $(ele).parent().addClass("has-error");
                $(ele).next("span").text(msg);
            } else if ("success" === status) {
                $(ele).parent().addClass("has-success");
                $(ele).next("span").text(msg);
            }
        }

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
            totalPage = pageInfo.total;
            $("#page_info").append("当前第 " + pageInfo.pageNum + " 页，" +
                "总共有 " + pageInfo.pages + " 页，总记录 " + pageInfo.total + " 条数据。");
        }

        function build_page_nav(result) {
            console.log(result.map.pageInfo);
            $("#page_nav").empty();
            var $ul = $("<ul></ul>").addClass("pagination");
            var pageInfo = result.map.pageInfo
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
                    to_page(pageInfo.pages);
                });
            $ul.append(navigatepageLi)
                .append(nextPageLi)
                .append(lastPageLi)
                .appendTo("#page_nav");

        }

        function getDept() {
            $.ajax({
                url: "/depts",
                type: "get",
                success: function (result) {
                    console.log(result.map.departmentList);
                    var departmentList = result.map.departmentList;
                    $.each(departmentList, function (index, item) {
                        /**
                         * 动态创建部门的下拉列表
                         */
                        $("<option></option>").append(item.deptName)
                            .attr("value", item.deptId).appendTo("#dept1");
                    });
                }
            });
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
            <button class="btn btn-primary" id="emp_add_modal">新增</button>
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


<%--
    员工添加的模态框
--%>
<div class="modal fade" tabindex="-1" role="dialog" id="emp_add">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">员工添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="add_emp_form">
                    <div class="form-group">
                        <label for="emp_name" class="col-sm-2 control-label">empName</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="empName" id="emp_name"
                                   placeholder="empName">
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="emp_email" class="col-sm-2 control-label">emp_email</label>
                        <div class="col-sm-10">
                            <input type="email" name="empEmail" class="form-control" id="emp_email"
                                   placeholder="emp_email">
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">emp_email</label>
                        <div class="col-sm-10">
                            <label class="radio-inline">
                                <input type="radio" name="empGender" id="gender1_add" value="0" checked>男
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="empGender" value="1" id="gender2_add">女
                            </label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">deptName</label>
                        <div class="col-sm-10">
                            <select id="dept1" class="form-control" name="dId"></select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="add_emp_btn">添加</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>

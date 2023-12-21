<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>

    <style>
        /* 추가 스타일링을 위한 CSS */
        tr:hover {
            background-color: #f5f5f5;
            cursor: pointer;
        }
        #table_body tr:hover {
            background-color: #f5f5f5;
            cursor: pointer;
        }


        header {
            height: 55px;
        }
        table{
            width: 100%;
        }
    </style>

    <script type="text/javascript">
        $(function (){
            doubliClickEvents();
        })

        $(function() {

            $.ajax({
                url			: '/main_header_3',
                dataType 	: 'html',
                success		: function(data) {
                    $('#header').html(data);
                }
            });

            $.ajax({
                url			: '/main_footer',
                dataType 	: 'html',
                success		: function(data) {
                    $('#footer').html(data);
                }
            });
        });

        function doubliClickEvents(){
            var trElements = document.querySelectorAll('.cont');

            console.log(trElements.length);
            trElements.forEach(function (trElement) {
                console.log("fdsfds");
                trElement.addEventListener('dblclick', function () {
                    var facility_code = trElement.getAttribute('data-doc-no');
                    window.location.href = "/selectcheckReportlist2?facility_code=" + facility_code+"&currentPage=1";
                    // getcheckresultform
                });
            });
        }

        let facility_category;
        let org_area;
        let org_name;
        let user_department;
        let firstdate;
        let secdate;
        let cate_name;
        let facility_code;
        let research;

        function searchCheckReportList(currentPage, search) {

            let wr;


            if (search) {
                facility_category= $("#facility_category_List").val();
                org_area= $("#org_area_List").val();
                org_name= $("#org_name_List").val();
                user_department=$("#user_department").val();
                firstdate= $("#find_date1").val();
                secdate= $("#find_date2").val();
                cate_name= $("#cate_name").val();
                facility_code= $("#facility_code").val();
                research= $("#research").val();
            }

            wr = {
                currentPage: currentPage,
                facility_category: facility_category,
                org_area: org_area,
                org_name: org_name,
                user_department: user_department,
                firstdate: firstdate,
                secdate: secdate,
                cate_name: cate_name,
                facility_code: facility_code,
                research: research
            };

            console.log(wr);
            $.ajax({
                url: "/getcheckresultform",
                data: wr,
                success(data) {
                    console.log(data);
                    const table_body = $("#table_body");
                    table_body.empty();

                    const { objList, obj } = data;
                    function addCell(value) {
                        let result = $('<td></td>');
                        if (value == null) {
                            result.append('없음');

                        }else {
                            result.append(value);
                        }


                        return result;
                    }
                    console.log("obj");
                    console.log(obj);

                    objList.forEach((values) => {
                        const newtr = $('<tr></tr>').addClass("cont").attr('data-doc-no', values.facility_code);

                        newtr.append(addCell(values.rn));
                        newtr.append(addCell(values.facility_category));
                        newtr.append(addCell(values.facility_code));
                        newtr.append(addCell(values.org_name));
                        newtr.append(addCell(values.org_area_name));
                        newtr.append(addCell(values.firstdate));
                        newtr.append(addCell(values.check_result));
                        newtr.append(addCell(values.user_name));

                        table_body.append(newtr);
                    });

                    const paginationDiv = $('#paging');
                    paginationDiv.empty();

                    let jspPagination = '';

                    if (obj.startPage > obj.pageBlock) {
                        jspPagination += '<div class="page-link" onclick="searchCheckReportList(' + (obj.startPage - obj.pageBlock) + ',false)">이전</div>';
                    }

                    for (var i = obj.startPage; i <= obj.endPage; i++) {
                        jspPagination += '<div class="page-item" onclick="searchCheckReportList(' + i + ',false)"><div class="page-link">' + i + '</div></div>';
                    }

                    if (obj.endPage >= obj.pageBlock) {
                        jspPagination += '<div class="page-link" onclick="searchCheckReportList(' + (obj.startPage + obj.pageBlock) + ',false)">다음</div>';
                    }
                    jspPagination += '</div>';

                    paginationDiv.html(jspPagination);
                    doubliClickEvents();
                }
            });


        }


    </script>
</head>
<body>

<header id="header"></header>




<div class="container" style="margin-top: 3%">
    <div class="row">
        <div id="center">
            <form action="" id="">
                <table>
                    <tr>
                        <td>시설물 종류:</td>
                        <td>
                            <select class="form-select" id="facility_category_List">
                                <c:forEach items="${wrctgList}" var="list">
                                    <option name="facility_category"
                                            value="${list.facility_category}">${list.facility_category}</option>
                                </c:forEach>
                                <option name="facility_category"
                                        value="전체" selected>전체
                                </option>
                            </select></td>
                        <td>행정구역:</td>
                        <td>
                            <select class="form-select" id="org_area_List">
                                <c:forEach items="${orgList}" var="list">
                                    <option name="org_area" value="${list.org_area_name}">${list.org_area_name}</option>
                                </c:forEach>
                                <option name="org_area"
                                        value="전체" selected>전체
                                </option>
                            </select>
                        </td>
                        <td>관리기관:</td>

                        <td>
                            <select class="form-select" id="org_name_List">
                                <c:forEach items="${organizationList}" var="list">
                                    <option name="org_name" value="${list.org_name}">${list.org_name}</option>
                                </c:forEach>
                                <option name="org_name"
                                        value="전체" selected>전체
                                </option>
                            </select>
                        </td>
                        <td>점검자 소속:</td>
                        <td><input type="text" id="user_department" class="form-control"></td>

                    </tr>
                    <tr>
                        <td>조회기간:</td>
                        <td><input type="date" name="find_date1" id="find_date1" class="form-control"></td>
                        <td><p style="text-align: center">~</p></td>
                        <td><input type="date" name="find_date2" id="find_date2" class="form-control"></td>

                        <td>시설물 명칭:</td>
                        <td><input type="text" name="cate_name" id="cate_name" class="form-control"></td>
                        <td>시설물 코드:</td>
                        <td><input type="text" name="facility_code" id="facility_code" class="form-control"></td>

                        <td><input type="button" onclick="searchCheckReportList(1,true)" value="조회" class="btn btn-dark"></td>
                        <td><input type="checkbox" id="research">결과 내 재검색</td>

                    </tr>
                </table>
            </form>
            <table class="table ">
                <tr>
                    <th>연번</th>
                    <th>시설물 종류</th>
                    <th>시설물 코드</th>
                    <th>행정구역</th>
                    <th>관리기관</th>
                    <th>최종점검일</th>
                    <th>최종점검결과</th>
                    <th>점검자</th>
                </tr>

                <tbody id="table_body">

                <%--                    <tbody id="table_body">--%>
                <c:forEach items="${wrList}" var="list">
                    <tr class="cont" data-doc-no="${list.facility_code}">
                        <td>${list.rn}</td>
                        <td>${list.facility_category}</td>
                        <td>${list.facility_code}</td>
                        <td>${list.org_name}</td>
                        <td>${list.org_area_name}</td>
                        <c:if test="${list.firstdate == null}">
                            <td>점검없음</td>
                        </c:if>
                        <c:if test="${list.firstdate != null}">
                            <td>${list.firstdate}</td>
                        </c:if>

                        <c:if test="${list.check_result == null}">
                            <td>점검결과없음</td>
                        </c:if>
                        <c:if test="${list.check_result != null}">
                            <td>${list.check_result}</td>
                        </c:if>

                        <c:if test="${list.user_name == null}">
                            <td>점검자없음</td>
                        </c:if>
                        <c:if test="${list.user_name != null}">
                            <td>${list.user_name}</td>
                        </c:if>

                    </tr>
                </c:forEach>


                </tbody>

            </table>

            <div id="paging" class="pagination justify-content-center">
                <c:if test="${page.startPage > page.pageBlock}">
<%--                    <div class="page-link" onclick="location.href='/selectCheckReportList?currentPage=${page.startPage - page.pageBlock}'">--%>
                        <div class="page-link" onclick="searchCheckReportList(${page.startPage - page.pageBlock}, false)">

                        이전
                    </div>
                </c:if>
                <c:forEach var="i" begin="${page.startPage}" end="${page.endPage}">
<%--                    <div class="page-item" onclick="location.href='/selectCheckReportList?currentPage=${i}'">--%>
                    <div class="page-item" onclick="searchCheckReportList(${i},false)">
                        <div class="page-link" style="cursor:pointer">${i}</div>
                    </div>
                </c:forEach>
                <c:if test="${page.endPage >= page.pageBlock}">
<%--                    <div class="page-link" onclick="location.href='/selectCheckReportList?currentPage=${page.startPage + page.pageBlock}'">--%>
                    <div class="page-link" onclick="searchCheckReportList(${page.startPage + page.pageBlock}, false)">
                        다음
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>



<footer class="footer py-2">
    <div id="footer" class="container">
    </div>
</footer>

</body>

</html>

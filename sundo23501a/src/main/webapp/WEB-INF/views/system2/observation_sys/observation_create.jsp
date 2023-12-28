<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>실시간 수문정보 관리시스템 - 관측소 등록</title>

<style>
header {
	height: 55px;
}
table {
	border-collapse: collapse;
	width: 100%;
}
th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: left;
}
th {
	text-align: center;
	background-color: #f2f2f2;
}
.btn_group{
	margin-top: 15%;
	margin-bottom: 2%;
	text-align: center;
}
.underline {
	border-bottom:2px solid #fff;
}
.doc-title table {
	widtd:100%;
	height:45px;
}
.doc-title th,
.doc-title td {
	border:0px;
	border-bottom:3px solid #2C3E50;
}
</style>
<script>
	$(function() {

		$.ajax({
			url			: '/main_header_2',
			async		: false,
			dataType 	: 'html',
			success		: function(data) {
				$('#header').html(data);
			}
		});
		$("#sub-list-1").addClass('underline');

		$.ajax({
			url			: '/main_footer',
			dataType 	: 'html',
			success		: function(data) {
				$('#footer').html(data);
			}
		});
	});
</script>
</head>
<body>
<body class="pt-3">

	<header id="header"></header>
	
	<div class="container"> 
		<div class="card" style="padding:30px">
			<form action="ob_create" method="post">
				<table class="doc-title">
					<tr>
						<td style="vertical-align:top"><span class="apptitle">관측소 등록</span></td>
						<td>
							<div align="right">
								<button type="button" class="btn btn-secondary" onclick="location.href='/observation_find'">
									<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-card-list" viewBox="0 0 16 16">
		                            <path d="M14.5 3a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h13zm-13-1A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-13z"/>
		                            <path d="M5 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 5 8zm0-2.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm0 5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm-1-5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zM4 8a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zm0 2.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0z"/></svg>
								    목록으로
								</button>
							</div>													
						</td>
					</tr>
				</table>
				<br>
				<table>
					<tr>
						<th>관측소명</th>
						<td colspan='3'><input type="text" name="observe_post" class="form-control"></td>
					</tr>
					<tr>
						<th>관측유형</th>
						<td>
							<select name="observe_type" class="form-select">
								<c:forEach var="code" items="${CodeObserveType}">
									<option value="${code.cate_code}">${code.cate_name}</option>
								</c:forEach>
							</select>
						</td>
						<th>표준코드</th>
						<td>
							<select name="river_code" class="form-select">
								<c:forEach var="river" items="${categoryList}">
									<option value="${river.river_code}">${river.river_code}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>위도</th>
						<td><input type="text" name="latitude" class="form-control"></td>
						<th>경도</th>
						<td><input type="text" name="longitude" class="form-control"></td>
					</tr>
					<tr>
						<th>관측방식</th>
						<td>
							<select name="observe_method" class="form-select">
								<c:forEach var="code" items="${CodeObserveMethod}">
									<option value="${code.cate_code}">${code.cate_name}</option>
								</c:forEach>
							</select>
						</td>

						<th>운영기관</th>
						<td>
							<select name="org_code" class="form-select">
								<c:forEach var="org" items="${OrgList}">
									<option value="${org.org_code}">${org.org_name}</option>									
								</c:forEach>
							</select>
							<input type="hidden" name="org_area" value="${org.org_area}">
						</td>
					</tr>
				</table>
					
				<div class="btn_group">
					<button type="submit" class="btn btn-dark">
					 <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clipboard2" viewBox="0 0 16 16">
                           <path d="M3.5 2a.5.5 0 0 0-.5.5v12a.5.5 0 0 0 .5.5h9a.5.5 0 0 0 .5-.5v-12a.5.5 0 0 0-.5-.5H12a.5.5 0 0 1 0-1h.5A1.5 1.5 0 0 1 14 2.5v12a1.5 1.5 0 0 1-1.5 1.5h-9A1.5 1.5 0 0 1 2 14.5v-12A1.5 1.5 0 0 1 3.5 1H4a.5.5 0 0 1 0 1z"/>
                           <path d="M10 .5a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5.5.5 0 0 1-.5.5.5.5 0 0 0-.5.5V2a.5.5 0 0 0 .5.5h5A.5.5 0 0 0 11 2v-.5a.5.5 0 0 0-.5-.5.5.5 0 0 1-.5-.5"/></svg>
					    등록
					</button>
				</div>
			</form>
		</div>
	</div>

	<footer class="footer py-2">
		<div id="footer" class="container">
		</div>
	</footer>
</body>
</html>
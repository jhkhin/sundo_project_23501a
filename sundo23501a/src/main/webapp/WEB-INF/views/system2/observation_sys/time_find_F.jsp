<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>실시간 수문정보 관리시스템 - 일자료(우량)</title>
<style type="text/css">
header {
	height: 55px;
}
.searchbox {
	border:2px solid #dee2e6;
	border-radius: 10px;
	padding: 10px;
}
.listTable {
	width: 100%;
	text-align: center;
}
.tableCate {
	border-top: solid gray 2px;
	background: #EAEAEA;
	height: 35px;
}
.tableCate th {
	border-left: solid white 1px;
	border-right: solid white 1px;
}
.tableRow {
	border-bottom: solid lightgray 1px;
	height: 35px;
}
#paging {
	margin: 30px;
}
.underline {
	border-bottom:2px solid #fff;
}
.underline-tab {
	border-bottom:3px solid #00aaff;
}
.sub-tab {
	color:#000088;
	font-weight:bold;
	cursor:pointer;
	margin:0px 5px;
	padding:5px;
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
		$("#sub-tab-3").addClass('underline-tab');

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

	<header id="header"></header>
	<div class="container">
		<input type="hidden" name="river_code" value="${flowList.get(0).river_code}">
		<input type="hidden" name="observe_year" value="${flowList.get(0).observe_year}">

		<div align="right" style="margin:10px 0px">
			<button type="button" class="btn btn-secondary" onclick="location.href='observation_find'">
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-card-list" viewBox="0 0 16 16">
                <path d="M14.5 3a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h13zm-13-1A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-13z"/>
                <path d="M5 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 5 8zm0-2.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm0 5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm-1-5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zM4 8a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zm0 2.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0z"/></svg>
		     	목록으로
			</button>
		</div>

		<!-- 검색 -->
		<div class="searchbox">
			<table>
				<tr>
					<th style="width:70px;text-align:right;font-weight:bold;">자료유형</th>
					<td>
						<select id="type" class="form-select" >
							<option value="hour" selected="selected">시자료</option>
							<option value="statistics">통계</option>
						</select></td>
					<th style="width:70px;text-align:right;font-weight:bold;">년도</th>
					<td>
						<select id="year" class="form-select">
							<c:forEach var="FlowYear" items="${flowYearList}">
								<option value="${FlowYear.observe_year}">${FlowYear.observe_year}</option>
							</c:forEach>
						</select>
					</td>	
					<td style="padding-left:10px">
						<button onclick="ob_search()" class="btn btn-dark btn-sm" >검색</button>
					</td>
				</tr>
			</table>
		</div>
			
		<!-- 태그 -->
		<div class="searchbox" style="margin-top:10px">
			<table style="margin:5px 30px">
				<tr>
					<th width="100"><span class="sub-tab" id="sub-tab-1"><a href="/time_find?river_code=${flowList.get(0).river_code}">수위정보</a></th>
					<th width="100"><span class="sub-tab" id="sub-tab-2"><a href="/time_find_R?river_code=${flowList.get(0).river_code}">강우량정보</a></th>
					<th width="100"><span class="sub-tab" id="sub-tab-3"><a href="/time_find_F?river_code=${flowList.get(0).river_code}">우량정보</a></th>
					<%-- <th width="100"><span class="sub-tab" id="sub-tab-4"><a href="/time_find_F?river_code=${flowList.get(0).river_code}">수문정보</a></th> --%>
				</tr>
			</table>
		</div>
		
		<div style="margin-top:20px">
			<input type="hidden" name="river_code" value="${flowList.get(0).river_code}">
			<span class="apptitle">일자료(우량)</span>
			<h5><b>관측소명 : <font class="text-info">${flowList.get(0).observe_post}</font></b></h5>
			<h6><b>(단위:mm)</b></h6>
			<div align="right" style="padding:10px 0px">
				<button type="button" class="btn btn-secondary pull-right" onclick="ob_save()">
	                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clipboard2-check-fill" viewBox="0 0 16 16">
	                <path d="M10 .5a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5.5.5 0 0 1-.5.5.5.5 0 0 0-.5.5V2a.5.5 0 0 0 .5.5h5A.5.5 0 0 0 11 2v-.5a.5.5 0 0 0-.5-.5.5.5 0 0 1-.5-.5"/>
	                <path d="M4.085 1H3.5A1.5 1.5 0 0 0 2 2.5v12A1.5 1.5 0 0 0 3.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-12A1.5 1.5 0 0 0 12.5 1h-.585c.055.156.085.325.085.5V2a1.5 1.5 0 0 1-1.5 1.5h-5A1.5 1.5 0 0 1 4 2v-.5c0-.175.03-.344.085-.5m6.769 6.854-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 9.793l2.646-2.647a.5.5 0 0 1 .708.708Z"/></svg>
	                저장
	             </button>
			</div>
			<table class="listTable">
				<tr class="tableCate">
					<th>관측일</th>
					<c:forEach var="cnt" begin="1" end="12" step="1">
						<th>${cnt}월</th>
					</c:forEach>
				</tr>
				<tbody id="searchF">
				<c:forEach var="cnt" begin="0" end="${fn:length(flowList)-1}" step="1">
					<tr class="tableRow">	
						<td><a href="/time_edit_F?river_code=${flowList.get(cnt).river_code}&observe_year=${flowList.get(cnt).observe_year}&observe_day=${flowList.get(cnt).observe_day}">${flowList.get(cnt).observe_day}일</a></td>
			 			<td>${flowList.get(cnt).january}</td>
						<td>${flowList.get(cnt).february}</td>
						<td>${flowList.get(cnt).march}</td>
						<td>${flowList.get(cnt).april}</td>
						<td>${flowList.get(cnt).may}</td>
						<td>${flowList.get(cnt).june}</td>
						<td>${flowList.get(cnt).july}</td>
						<td>${flowList.get(cnt).august}</td>
						<td>${flowList.get(cnt).september}</td>
						<td>${flowList.get(cnt).october}</td>
						<td>${flowList.get(cnt).november}</td>
						<td>${flowList.get(cnt).december}</td>	 
					</tr>	
				 </c:forEach>
				 </tbody>
			</table>
		</div>
	</div>

	<footer class="footer py-2">
		<div id="footer" class="container">
		</div>
	</footer>
</body>
</html>
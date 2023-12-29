<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수자원 시설물 관리시스템 - 고장 보고서</title>
<style type="text/css">
	.title {
		text-align: center;
		font-size: 25px;
		font-weight: bold;
	}
	
	table {
		width: 100%;
		border-top: solid gray 2px;
		border-bottom: solid gray 2px;
	}
	
	tr {
		border-bottom: solid lightgray 1px;
		border-top: solid lightgray 1px;
	}
	
	th {
		background: #EAEAEA;
		padding: 5px 10px;
		height: 40px;
	}
	
	td {
		padding: 5px 10px;
		height: 40px;
	}
	
	select {
		width: 100%;
		height: 35px;
		border: none;
	}
	
	textarea {
		width: 100%;
	}
	
	.subject {
		width: 100%;
		height: 35px;
	}
	
	.break_date {
		height: 35px;
		width: 200px;
	}
	
	.handling_content {
		width: 95%;
		height: 35px;
	}
	
	.handling_flag {
		width: 4%;
	}
	
	.btns {
		text-align: right;
		margin-top: 20px;
	}
	
	button {
		width: 80px;
	}
	
	.rptTbl th {
		width: 150px;
		height: 40px;
	}

	.alarmTbl .cate {
		width: 150px;
		height: 40px;
	}
	
	header {
		height: 55px;
	}
.underline {
	border-bottom:2px solid #fff;
}
</style>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script type="text/javascript">

	$(function() {
	
		$.ajax({
			url			: '/main_header_3',
			async		: false,
			dataType 	: 'html',
			success		: function(data) {
				$('#header').html(data);
			}
		});
		$("#sub-list-4").addClass('underline');
	
		$.ajax({
			url			: '/main_footer',
			dataType 	: 'html',
			success		: function(data) {
				$('#footer').html(data);
			}
		});
	});

	$(function() {
		$('#waterCategorySelect').change(function() {
			var category = $(this).val();
			var sendurl = 'get_facility_code?facility_category=' + category;
			
			$.ajax({
				url		: sendurl,
				dataType: 'json',
				success : function(rtndata) {
					console.log(rtndata);
					
					var wrCodeOption = $('#wrCodeOption');
					var options = "";
					
					options += '<select id="wrCodeSelect" name="facility_code">';
					options += '<option>전체</option>';
					
					for(var i = 0; i < rtndata.length; i++) {
						options += '<option value="' + rtndata[i].facility_code + '">' + rtndata[i].facility_code + '</option>';
					}
					
					options += '</select>';
	
					wrCodeOption.empty();
					wrCodeOption.append(options);
					
					$('#wrCodeSelect').change(function() {
						var wrCode = $(this).val();
						var sendurl = 'get_alarm?facility_code=' + wrCode;
						
						$.ajax({
							url		: sendurl,
							dataType: 'json',
							success	: function(rtndata) {
								console.log(rtndata);
								
								var alarmBox = $('#alarmBox');
								var alarms = "";
								
								alarms += '<tr><th rowspan="' + rtndata.length+1 + '">알람내역</th><th>알람코드</th><th>알람내용</th><th>알람일시</th><th>확인</th></tr>';
								
								for(var i = 0; i < rtndata.length; i++) {
									alarms += '<tr><td>' + rtndata[i].alarm_code + '</td>' 
											+ '<td>' + rtndata[i].alarm_content	+ '</td>'
											+ '<td>' + rtndata[i].alarm_date + '</td>'
											+ '<td><input type="checkbox" name="alarm_code" value="' + rtndata[i].alarm_code + '"></td></tr>';
								}
								
								alarmBox.empty();
								alarmBox.append(alarms);
							}
						});
					});
				}
			});
		});
	});
	
</script>
</head>
<body>
	<header id="header"></header>
	
	<div class="container" style="margin-top: 3%">
		<div class="row">
			<div id="center">
				<p class="title">고장 보고서 수정</p>
				<form action="error_report_update" method="post">
					<input type="hidden" name="doc_no" value="${breakReport.doc_no}">
					<input type="hidden" name="break_report_no" value="${breakReport.doc_no}">
					<table class="rptTbl">
						<thead>
							<tr>
								<th>시설물 종류</th>
								<td>
									<select id="waterCategorySelect" name="facility_category">
										<c:forEach var="water" items="${waterCategory }">
											<c:if test="${water.facility_category == breakReport.facility_category }">
												<option value="${water.facility_category }" selected="selected">${water.facility_category }</option>
											</c:if>
											<c:if test="${water.facility_category != breakReport.facility_category }">
												<option value="${water.facility_category }">${water.facility_category }</option>
											</c:if>
										</c:forEach>
									</select>
								</td>
								<th>시설물 코드</th>
								<td>
									<div id="wrCodeOption">
										<select id="wrCodeSelect" name="facility_code">
											<c:forEach var="wrCodeList" items="${wrCodeList }">
												<c:if test="${wrCodeList.facility_code == breakReport.facility_code}">
													<option value="${wrCodeList.facility_code }" selected="selected">${wrCodeList.facility_code }</option>
												</c:if>
												<c:if test="${wrCodeList.facility_code != breakReport.facility_code}">
													<option value="${wrCodeList.facility_code }">${wrCodeList.facility_code }</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</td>
								<th>작성자</th>
								<td><input type="hidden" name="user_id" value="${breakReport.user_id }">${breakReport.user_name }</td>	<!-- 로그인한 사용자 이름 자동 표출 필요 -->
							</tr>
							<tr>
								<th>점검대상</th>
								<td>
									<select name="check_target">
										<c:forEach var="checkCode" items="${checkCodeList }">
											<c:choose>
												<c:when test="${checkCode.field_name == 'check_target' && checkCode.cate_name == breakReport.check_target}">
													<option value="${checkCode.cate_name }" selected="selected">${checkCode.cate_name }</option>
												</c:when>
												<c:when test="${checkCode.field_name == 'check_target' && checkCode.cate_name != breakReport.check_target}">
													<option value="${checkCode.cate_name }">${checkCode.cate_name }</option>
												</c:when>
											</c:choose>
										</c:forEach>
									</select>
								</td>
								<th>중분류</th>
								<td>
									<select name="m_category">
										<c:forEach var="checkCode" items="${checkCodeList }">
											<c:choose>
												<c:when test="${checkCode.field_name == 'm_category' && checkCode.cate_name == breakReport.m_category}">
													<option value="${checkCode.cate_name }" selected="selected">${checkCode.cate_name }</option>
												</c:when>
												<c:when test="${checkCode.field_name == 'm_category' && checkCode.cate_name != breakReport.m_category}">
													<option value="${checkCode.cate_name }">${checkCode.cate_name }</option>
												</c:when>
											</c:choose>
										</c:forEach>
									</select>
								</td>
								<th>소분류</th>
								<td>
									<select name="s_category">
										<c:forEach var="checkCode" items="${checkCodeList }">
											<c:choose>
												<c:when test="${checkCode.field_name == 's_category' && checkCode.cate_name == breakReport.s_category}">
													<option value="${checkCode.cate_name }" selected="selected">${checkCode.cate_name }</option>
												</c:when>
												<c:when test="${checkCode.field_name == 's_category' && checkCode.cate_name != breakReport.s_category}">
													<option value="${checkCode.cate_name }">${checkCode.cate_name }</option>
												</c:when>
											</c:choose>
										</c:forEach>
									</select>
								</td>
							</tr>
						</thead>
						<tbody>
							<tr><th>제목</th><td colspan="5"><input type="text" class="subject" name="subject" value="${breakReport.subject }"></td></tr>
							<tr><th>고장일자</th><td colspan="5"><input type="date" class="break_date" name="break_date" value="${breakReport.break_date }"></td></tr>
							<tr><th>고장원인</th><td colspan="5"><textarea rows="4" name="break_cause">${breakReport.break_cause }</textarea></td></tr>
							<tr><th>현재상황</th><td colspan="5"><textarea rows="4" name="current_state">${breakReport.current_state }</textarea></td></tr>
							<tr><th>즉시처리</th>
								<td colspan="5">
									<c:choose>
										<c:when test="${breakReport.handling_flag == 'Y' }">
											<input type="checkbox" class="handling_flag" name="handling_flag" value="Y" checked="checked">
										</c:when>
										<c:otherwise>
											<input type="checkbox" class="handling_flag" name="handling_flag" value="N">
										</c:otherwise>
									</c:choose>
									<input type="text" class="handling_content" name="handling_content" value="${breakReport.handling_content }">
								</td></tr>
							<tr><th>향후계획</th><td colspan="5"><textarea rows="4" name="future_plan">${breakReport.future_plan }</textarea></td></tr>
						</tbody>
					</table>
					
					<table class="alarmTbl">
						<tbody id="alarmBox">
		 					<tr>
								<th rowspan="50" class="cate">알람내역</th>
								<th>알람코드</th><th>알람내용</th><th>알람일시</th><th>확인</th>
							</tr>
							<c:forEach var="rptAlarm" items="${rptAlarmList }">
								<tr>
									<td>${rptAlarm.alarm_code }</td><td>${rptAlarm.alarm_content }</td><td>${rptAlarm.alarm_date }</td>
									<td><input type="checkbox" name="alarm_code" value="${alarm.alarm_code }" checked="checked"></td>
								</tr>
							</c:forEach>
							<c:forEach var="alarm" items="${alarmList }">
								<tr>
									<td>${alarm.alarm_code }</td><td>${alarm.alarm_content }</td><td>${alarm.alarm_date }</td>
									<td><input type="checkbox" name="alarm_code" value="${alarm.alarm_code }"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="btns">
						<button type="button" class="btn btn-dark btn-sm" onclick="location.href='/error_report_read?doc_no=${breakReport.doc_no}'">돌아가기</button>
						<button type="submit" class="btn btn-dark btn-sm">저장</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<footer class="footer py-2">
		<div id="footer" class="container">
		</div>
	</footer>
</body>
</html>
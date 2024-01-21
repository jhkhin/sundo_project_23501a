<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
	
.chartMenu {
	border: solid 1px silver;
	padding: 20px;
	width: 20%;
}

.chart {
	width: 80%;
	padding: 0px 50px;
}

.chart p {
	text-align: center;
	padding: 15px;
	font-size: 17pt;
	font-weight: bold;
}
		
</style>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script>

	var monthArray = new Array(32);

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
		
		// 시자료 눌렀을때 시자료 페이지, 통계누르면 통계 페이지
 		$('select[name=selectType]').change(function(){
			var type = $(this).val();
			if(type == "hour") {
				//시자료 hour
				location.href = "/time_find_F?river_code="+$("#river_code").val();
			}else{
				//통계 statistics
				$(location).attr("href", "/time_chart_list_F?river_code="+$("#river_code").val()+"&observe_year="+$("#year").val());
			}
		});
		
		$('#year').change(function(){
			searchFlowData();
		});

		//통계 데이타 가져오기
		searchFlowData();
		
	});
	
	function searchFlowData() {
		var p_river_code = $("#river_code").val();
		var p_year = $("#year").val();
		if(p_year == null) {
			p_year = $("#observe_year").val();
		}
		//alert("p_year:"+p_year);
		
		$.ajax({
			url			: '/time_chart_search_F',
			data		: {river_code:p_river_code, observe_year:p_year},
			dataType	: 'json',
			success		: function(data) {
				console.log(data);
				displayData(data);
			}
		});
	}
	
	function displayData(list) {
		
		//배열 초기화		
		for (var i = 0; i <= 31; i++) {
			monthArray[i] = [i,0,0,0,0,0,0,0,0,0,0,0,0];
		}
		console.log(monthArray);

		$.each(list, function (index, item) {
			var river_code = item.river_code;
			var year = item.observe_year;
			var day = item.observe_day;
			
			monthArray[day][1] = (item.january == null)?0:item.january;
			monthArray[day][2] = (item.february == null)?0:item.february;
			monthArray[day][3] = (item.march == null)?0:item.march;
			monthArray[day][4] = (item.april == null)?0:item.april;
			monthArray[day][5] = (item.may == null)?0:item.may;
			monthArray[day][6] = (item.june == null)?0:item.june;
			monthArray[day][7] = (item.july == null)?0:item.july;
			monthArray[day][8] = (item.august == null)?0:item.august;
			monthArray[day][9] = (item.september == null)?0:item.september;
			monthArray[day][10] = (item.october == null)?0:item.october;
			monthArray[day][11] = (item.november == null)?0:item.november;
			monthArray[day][12] = (item.december == null)?0:item.december;
		});
		
		console.log(monthArray);
		
		google.charts.load('current', {packages: ['corechart', 'line']});
		google.charts.setOnLoadCallback(drawCurveTypes);
	}	

	function drawCurveTypes() {
		
		var data = new google.visualization.DataTable();
		data.addColumn('number', 'X');
		data.addColumn('number', '1월');
		data.addColumn('number', '2월');
		data.addColumn('number', '3월');
		data.addColumn('number', '4월');
		data.addColumn('number', '5월');
		data.addColumn('number', '6월');
		data.addColumn('number', '7월');
		data.addColumn('number', '8월');
		data.addColumn('number', '9월');
		data.addColumn('number', '10월');
		data.addColumn('number', '11월');
		data.addColumn('number', '12월');

		data.addRows(monthArray);

		var options = {
			hAxis: {
				title: '날짜'
			},
			vAxis: {
				title: '우량'
			},
			series: {
				1: {curveType: 'function'}
			},
			width: 1000,
	        height: 500
		};

		var chart = new google.visualization.LineChart(document.getElementById('myChart'));
		chart.draw(data, options);
	}
	
</script>
</head>
<body>
	<header id="header"></header>
	<br>
	<div class="container">
		<input type="hidden" name="river_code" id="river_code" value="${flowList.get(0).river_code}">
		<input type="hidden" name="observe_year" id="observe_year" value="${flowList.get(0).observe_year}">
		
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
						<select id="type"  name="selectType" class="form-select">
							<option value="hour">시자료</option>
							<option value="statistics" selected="selected">통계</option>
						</select>
					</td>
					<th style="width:70px;text-align:right;font-weight:bold;">년도</th>
					<td>
						<select id="year" class="form-select">
							<c:forEach var="FlowYear" items="${flowYearList}">
								<option value="${FlowYear.observe_year}">${FlowYear.observe_year}</option>
							</c:forEach>
						</select>
					</td>
					<td style="padding-left:10px">
						<button onclick="searchFlowData()" class="btn btn-dark btn-sm" >검색</button>
					</td>
				</tr>
			</table>
		</div>
		<!-- 태그 -->
		<div class="searchbox" style="margin-top:10px">
			<table style="margin:5px 30px">
				<tr>
					<th width="100"><span class="sub-tab" id="sub-tab-1"><a href="/time_chart_list?river_code=${flowList.get(0).river_code}">수위정보</a></span></th>
					<th width="100"><span class="sub-tab" id="sub-tab-2"><a href="/time_chart_list_R?river_code=${flowList.get(0).river_code}">강우량정보</a></span></th>
					<th width="100"><span class="sub-tab" id="sub-tab-3"><a href="/time_chart_list_F?river_code=${flowList.get(0).river_code}">우량정보</a></span></th>
					<%-- <th width="100"><span class="sub-tab" id="sub-tab-4"><a href="/time_find_F?river_code=${flowList.get(0).river_code}">수문정보</a></span></th> --%>
				</tr>
			</table>
		</div>
		
		<div style="margin-top:20px">
			<input type="hidden" name="river_code" value="${flowList.get(0).river_code}">
			<span class="apptitle">우량정보</span>
			<h5><b>관측소명 : <font class="text-info">${flowList.get(0).observe_post}</font></b></h5>
			<h6><b>(단위:mm)</b></h6>
			<div align="right" style="padding:10px 0px">
			</div>
			
			<div class="chart">
				<p>통계</p>
				<div id="myChart"></div>
			</div>
			
		</div>
	</div>

	<footer class="footer py-2">
		<div id="footer" class="container">
		</div>
	</footer>
</body>
</html>
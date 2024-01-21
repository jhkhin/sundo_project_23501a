<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">

		.underline {
			border-bottom:2px solid #fff;
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

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

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
		
		
	 $(function() {
		 $.ajax({
			url			: '/chart_W' ,
			dataType	: 'json',
			success		: function(data){
				console.log(data);
				alert(data);
				var 
			}
		 })
		 
		 
	  const ctx = document.getElementById('myChart');
	
	  new Chart(ctx, {
	    type: 'bar',
	    data: {
	      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
	      datasets: [{
	        label: '# of Votes',
	        data: [12, 19, 3, 5, 2, 3],
	        borderWidth: 1
	      }]
	    },
	    options: {
	      scales: {
	        y: {
	          beginAtZero: true
	        }
	      }
	    }
	  });
  
 })
</script>
</head>
<body>
<header id="header"></header>
<br>
<div class="container">
	<div class="row">
		<div id="center">

			<div class="chart">
				<p></p>
				<p>통계</p>
			  	<canvas id="myChart"></canvas>
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
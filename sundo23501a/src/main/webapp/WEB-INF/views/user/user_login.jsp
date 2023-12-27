<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html lang="en" data-bs-theme="auto">
<head>
<meta charset="utf-8">
<title>관리시스템 Login</title>

<link href="/bootstrap-5.3.2-examples/assets/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="/bootstrap-5.3.2-examples/css/sign-in.css" rel="stylesheet">
<link href="/bootstrap-5.3.2-dist/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript">
	/* function chkInfo() {
	    // user_id와 user_pw의 값을 가져옵니다.
	    var userId = document.getElementById("user_id").value;
	    var userPw = document.getElementById("user_pw").value;
	
	    if (userId.trim() === "") {
	        document.getElementById("id_error").innerText = "아이디를 입력해주세요.";
	        return false;
	    } else {
	        document.getElementById("id_error").innerText = ""; // 에러 메시지 초기화
	    }
	
	    if (userPw.trim() === "") {
	        document.getElementById("pw_error").innerText = "비밀번호를 입력해주세요.";
	        return false;
	    } else {
	        document.getElementById("pw_error").innerText = ""; // 에러 메시지 초기화
	    }
	
	    return true;
	} */

</script>

<style type="text/css">
.wrapper{
    width: 100%;
    box-sizing: border-box;
    border: 2px solid lightgrey;
    border-radius: 10px;
    background-color: white;
    /* 가로 중앙 정렬을 위한 추가된 스타일 */
    margin: 0 auto;
    padding: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.login-wrapper{
	width: 330px;
}
.h2{
	font-weight: bold;
    font-size: 30px;
    color: #2C3E50;
    margin-bottom: 10px;
}
.h3{
	font-weight: bold;
    font-size: 20px;
    color: #2C3E50;
    margin-bottom: 10px;
    text-shadow: -3px 0px white, 0px 3px white, 3px 0px white, 0px -3px white;
}
.form-signin {
    max-width: 1050px;
}
.form-signin input[type="text"] {
    margin-bottom: -1px;
    border-bottom-right-radius: 0;
    border-bottom-left-radius: 0;
}
.water-title {
    font-weight: bold;
    font-size: 35px;
    color: #2C3E50;
    margin-bottom: 10px;
    text-align:center;
    text-shadow: -5px 0px white, 0px 5px white, 5px 0px white, 0px -5px white;
}
.radio-system-category{
	cursor:pointer;
}
.water-main-table {
	width: 100%;
	height: 100%;
	border: 0px solid #2C3E50;
	background-image: url(/images/seoulmap.png);
	position: relative;
    isolation: isolate;
    box-shadow: 0px 0px 24px 0px rgb(0 0 0 / 40%), 0px 0px 0px 0px rgb(0 0 0 / 40%);
}
.water-main-table::after {
	content: '';
	position: absolute;
	background: white;
	z-index: -1;
	inset: 0;
	opacity: 0.6;
}
.water-sub-table{
	width: 100%;
	height: 100%;
}
.info-table > th, td {
	padding:5px;
}
</style>
</head>
<body class="d-flex align-items-center py-4 bg-body-tertiary">

    
<main class="form-signin w-100 m-auto">

	<table class="water-main-table">
	<colgroup>
	<col width="*">
	<col width="410">
	</colgroup>
		<tr>
			<td>
				<table class="water-sub-table">
					<tr height="100">
						<td valign="top">
							<div>
								<h4><b>관리시스템</b></h4>
								<h2><p class="water-title">한강 수자원관리<br>종합플랫폼</p></h2>
							</div>
						</td>
					</tr>
					<tr height="*">
						<td align="center">
							<a href="/org_intro"><button class="btn btn-dark py-2" type="button">기관소개</button></a>
							<a href="/board_notice_list"><button class="btn btn-dark py-2" type="button">공지사항</button></a>
						</td>
					</tr>
					<tr height="150">
						<td>							
							<div class="text-center text-dark" style="font-size:20px">
								<img src="http://www.sundosoft.co.kr/images/intro/ci_logo1.png"><br>
								<b>중앙정보기술인재개발원 <font class="text-primary" style="font-size:40px;">A</font>조</b>
							</div>
							<div class="text-center"><b>이광현, 황인정, 강준우, 이진희, 조미혜</b></div>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<table class="water-sub-table">
					<tr>
						<td>
						
							<div id="water-tab" class="bd-example m-0 border-0">
								<nav>
									<div class="nav nav-tabs" id="nav-tab" role="tablist">
										<button style="background-color:white" class="nav-link active" id="nav-home-tab" data-bs-toggle="tab" data-bs-target="#nav-1" type="button" role="tab" aria-controls="nav-1" aria-selected="true" onclick="location.href='/user_login'"><b>로그인</b></button>
										<button style="background-color:white" class="nav-link" id="nav-profile-tab" data-bs-toggle="tab" data-bs-target="#nav-2" type="button" role="tab" aria-controls="nav-2" aria-selected="false" tabindex="-1" onclick="location.href='/user_join_write'"><b>회원가입</b></button>
									</div>
								</nav>
							</div>
						
							<form action="user_login_check" id="login-form" method="post">
								<div class="wrapper">
								<h4><b><span class="text-primary">로그인</span></b></h4>
									<div class="login-wrapper">
										
										<p class="text-primary">권한</p>
										<p><input type="radio" id="system_category_2" name="system_category" value="2" checked> <label class="radio-system-category" for="system_category_2">실시간 수문정보 관리시스템</label></p>
										<p><input type="radio" id="system_category_3" name="system_category" value="3"> <label class="radio-system-category" for="system_category_3">수자원 시설물 관리시스템</label></p>
										<p><input type="radio" id="system_category_1" name="system_category" value="1"> <label class="radio-system-category" for="system_category_1">관리자</label></p>
										
										
										<!-- 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다. -->
										<div class="form-floating">
											<input type="text" class="form-control" name="user_id" id="user_id" placeholder="ID" value="${user_id}">
											<label for="user_id">ID</label>
										</div>
										<div class="form-floating">
											<input type="password" class="form-control" name="user_pw" id="user_pw" placeholder="Password">
											<label for="user_pw">Password</label>
										</div>
										
										<c:if test="${idMsgBox != null}">
											<div class="invalid-feedback" style="display:block">${idMsgBox}</div>
										</c:if>
							
										<c:if test="${pwMsgBox != null}">
											<div class="invalid-feedback" style="display:block">${pwMsgBox}</div>
										</c:if>
										
										<div class="form-check text-start my-3">
											<input class="form-check-input" type="checkbox" value="remember-me" id="remember-check">
											<label class="form-check-label" for="remember-check">
												아이디 저장하기
											</label>
										</div>
										
										<button class="btn btn-primary w-100 py-2" type="submit">Login</button>
										
										<div style="margin-top:20px">
											<a href="user_find_pw"><input type="button" class="btn btn-secondary btn-sm" style="font-size:0.75rem" value="비밀번호 찾기"></a>
											<a href="user_find_id"><input type="button" class="btn btn-secondary btn-sm" style="font-size:0.75rem" value="아이디 찾기"></a>
											<a href="user_join_write"><input type="button" class="btn btn-outline-secondary btn-sm" style="font-size:0.75rem" value="회원가입"></a>
										</div>
										
										<p class="mt-5 text-body-secondary" align="center">&copy; 2023. 중앙정보기술인재개발원</p>
									</div>
								</div>
							</form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>	
</main>

</body>
</html>
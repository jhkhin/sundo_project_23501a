<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>한강 수자원관리 종합 플랫폼 - 공지사항</title>

<!--CSS START -->
<!-- CSS END -->

<!-- JS START -->
<script type="text/javascript" src="/js/board.js"></script>
<!-- JS END -->

<script type="text/javascript">
$(function() {
	$('#notify_flag_chkbox').click(function(){
		var checked = $(this).is(':checked');
		if(checked) {
			$(this).val("Y");
			$('input[name=notify_flag]').val("Y");
		}
		else {
			$(this).val("N");
			$('input[name=notify_flag]').val("N");
		}
	});
});

</script>
</head>
<body>
<!-- CONTENT -->
			<!------------------------------ //개발자 소스 입력 START ------------------------------->
				<div class="container-fluid">
					<form name="formPrjBdData" action="admin_notice_insert" method="post" enctype="multipart/form-data">
						<input type="hidden" name="user_id" value="${userInfoDTO.user_id}">
						<input type="hidden" name="bd_count" value="0">
						<input type="hidden" name="attach_name" value="">
						<input type="hidden" name="attach_path" value="">
						
						<div style="height:34px">
							<span class="apptitle">문서 작성</span>
						</div>
						<table width="100%" style="margin-top:7px">
							<tr>
								<td style="text-align:right">
									<button type="submit" class="btn btn-dark btn-sm">저장</button>
									<button type="button" class="btn btn-dark btn-sm" onclick="closeDoc()">닫기</button>
								</td>
							</tr>
						</table>
						<table class="table" width="100%">
							<colgroup>
								<col width="20%"></col>
								<col width="80%"></col>
							</colgroup>
							<tr>
								<th>작성자</th>
								<td><input type="text" class="form-control" name="user_name" value="${userInfoDTO.user_name}" readonly></td>
							</tr>
							<tr>
								<th>작성일</th>
								<td><input type="date" class="form-control" name="create_date_str" value="${todayDate}" readonly></td>
							</tr>
							<tr>
								<th>제목</th>
								<td><input type="text" class="form-control" name="subject" value="" required="required"></td>
							</tr>
							<tr>
								<th>파일첨부</th>
								<td><input type="file" class="form-control form-control-sm" name="file1"></td>
							</tr>
							<tr>
								<th>본문</th>
								<td>							
									<div class="input-group">
										<textarea class="form-control" aria-label="With textarea" name="body" rows="15"></textarea>
									</div>
								</td>
							</tr>
						</table>
					</form>
				</div>
				
			</div>
			</div>
	  		<!------------------------------ //개발자 소스 입력 END ------------------------------->
</body>
</html>
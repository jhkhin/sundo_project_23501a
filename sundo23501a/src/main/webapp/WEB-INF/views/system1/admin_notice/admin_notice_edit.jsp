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
					<form name="formPrjBdData" action="admin_notice_update" method="post" enctype="multipart/form-data">
						<input type="hidden" name="doc_no" value="${board.doc_no}">
						<input type="hidden" name="user_id" value="${board.user_id}">
						<div style="height:34px">
							<span class="apptitle">문서 수정</span>
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
								<td><input type="text" class="form-control" name="user_name" value="${board.user_name}" readonly></td>
							</tr>
							<tr>
								<th>작성일</th>
								
								<td><input type="text" class="form-control" name="create_date_str" value="<fmt:formatDate value="${board.create_date}" type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" readonly></td>
							</tr>
							<tr>
								<th>제목</th>
								<td><input type="text" class="form-control" name="subject" required="required" value="${board.subject}"></td>
							</tr>			
							<tr>
								<th>파일첨부</th>
								<td>
									<table width="100%">
										<tr>
											<td>
												<input type="hidden" name="attach_name" value="${board.attach_name}">
												<input type="hidden" name="attach_path" value="${board.attach_path}">
												<input type="hidden" name="attach_saved_name" value="${board.attach_saved_name}">
												<input type="hidden" name="attach_delete_flag" id="idAttachDeleteFlag" value="">
												<div id="idAttachFile">
													<c:if test="${board.attach_name ne null}">
														<a href="${board.attach_path}/${board.attach_saved_name}" target="_blank">${board.attach_name}</a>
														&nbsp;&nbsp;<img src="/images/btn_icon_delete2.png" onclick="deleteFlagAttach()" style="cursor:pointer">
													</c:if>													
												</div>																						
												<div id="idAttachInput" <c:if test="${board.attach_name ne null}">style="display:none;"</c:if> >
													<input type="file" class="form-control form-control-sm" name="file1">
												</div>
											</td>
										</tr>
									</table>								
								</td>
							</tr>
							<tr>
								<th>본문</th>
								<td>							
									<div class="input-group">
										<textarea class="form-control" aria-label="With textarea" name="body" rows="15">${board.body}</textarea>
									</div>
								</td>
							</tr>
						</table>
					</form>
			</div>
	  		<!------------------------------ //개발자 소스 입력 END ------------------------------->
</body>
</html>
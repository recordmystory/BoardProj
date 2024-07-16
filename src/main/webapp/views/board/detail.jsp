<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="alertMsg" value="${requestScope.alertMsg}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<!-- 부트스트랩 cdn -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<style>
	.content-area { 
		border: 1px solid black;
		border-radius: 10px;
        margin: 10px auto;
        padding: 10px;
        max-width: 80%; 
        height: auto;
        box-sizing: border-box; 
	}
	.content-title > h2 { font-weight: bold; text-align: center; margin: 15px 15px 15px 15px; }
	.btn-area { text-align: center; margin-top: 30px; }
	.table-area { margin: 70px 5px 0px 5px; border: 1px solid lightgray; border-radius: 8px; }
	.table-title { width: 200px; }
	#reply-table { text-align: center; }

</style>

<title>게시글 상세</title>
</head>
<body>
	<div class="container content-area">
		<div class="content-title">
			<h2>게시글 상세</h2>
		</div>
		<div class="table-area">
		<table class="table">
			<tr>
				<th class="table-title">제목</th>
				<td>${b.title}</td>
			</tr>
			<tr>
				<th class="table-title">작성일</th>
				<td>${b.regDate}</td>
			</tr>
			<tr>
				<th class="table-title">수정일</th>
				<td>${b.modDate}</td>
			</tr>
			<tr>
				<th class="table-title">내용</th>
				<td height="350px"><p style="white-space: pre-line;">${b.content}</p></td>
			</tr>
		</table>
		</div>
		<div class="btn-area">
			<a href="${contextPath}/list.bo" class="btn btn-primary btn-sm">글 목록</a>	
			<a href="${contextPath}/updateForm.bo?no=${b.no}" class="btn btn-secondary btn-sm">수정</a>
			<a href="${contextPath}/delete.bo?no=${b.no}" class="btn btn-danger btn-sm">삭제</a>
		</div>
		<br><br>
		
		<div class="reply-area">
			<table id="reply-table" class="table">
				<thead>
					<tr>
						<th style="align-content: center;">
							댓글 작성
						</th>
						<th width="650px">
							<textarea rows="3" class="form-control" style="resize: none;" id="reply-content"></textarea>
						</th>
						<td style="vertical-align: middle !important;">
							<button class="btn btn-success btn-sm" onclick="insertReply();">댓글 등록</button>
						</td>
					</tr>
				</thead>
				
				<tbody>
				
				</tbody>
			</table>
		</div>
	</div>
	
	<c:if test="${requestScope.alertMsg ne null}">
		<script>
	    	alert('${alertMsg}');
	    </script>
	</c:if>
	
	<c:remove var="alertMsg" scope="request" />
	
    <script>
    	$(function(){
    		
    		// 3초마다 select (ajax)
    		setInterval(selectReply, 3000);
    	});
    	
    	// 댓글 등록
    	function insertReply(){
    		let content = '';
    		
    		$.ajax({
    			url: '${contextPath}/replyinsert.bo',
    			data: { no: ${b.no}, content: $('#reply-content').val() },
    			method: 'post',
    			success: function(result) {
    				if(result > 0){
    					$('#reply-content').val(''); // textarea 초기화
    					selectReply(); // 갱신된 댓글 목록 조회해 화면에 뿌려주기
    				} else {
    					if(content == ''){
    						alert('댓글 내용을 입력해주세요.');
    					} else {
    						alert('댓글 작성 실패');
    					}
    				}
    			}, error: function(){
    				alert('댓글 작성에 실패했습니다. 잠시후 다시 시도해주세요.');
    			}
    			
    		});
    	};
    	
    	// 댓글 조회
    	function selectReply(){
    		$.ajax({
    			url: '${contextPath}/replylist.bo',
    			data: { no: ${b.no} },
    			success: function(list){
    				
    				let value = '';
    				
    				if(list.length > 0){
    					for(let i=0; i<list.length; i++){
    						value += '<tr>'
    							  + '<td>' + list[i].regId + '</td>'
    							  + '<td>' + list[i].content + '</td>'
    							  + '<td>' + list[i].regDate + '</td>'
    							  + '<tr>';
    							  
    					}
    				} else {
    					value += '<tr><td colspan="3">존재하는 댓글이 없습니다.</td></tr>';
    				}
    				
    				// table내에 list값 뿌리기
    				$('#reply-table tbody').html(value);
    			},
    			error: function(){
    				alert('댓글 조회에 실패했습니다. 잠시 후 시도해주세요.');
    			}
    			
    		});
    	}
    </script>
</body>
</html>
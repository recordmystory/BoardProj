<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="alertMsg" value="${sessionScope.alertMsg}" />

<!DOCTYPE html>
<html>
<head>

<style>
	.content-area { 
		border: 1px solid black;
		border-radius: 10px;
        margin: 10px auto;
        padding: 10px;
        max-width: 80%; 
        height: 750px;
        box-sizing: border-box; 
	}
	.content-title > h2 { font-weight: bold; text-align: center; margin: 15px 15px 15px 15px; cursor: pointer; }
	input[id='keyword'] { width: 200px; }
	.content-area-header > * { margin: 20px 10px 15px 0px; }
	tr { cursor: pointer; }
	#boardList > thead > tr  { text-align: center; }
	#boardList > tbody > tr  { text-align: center; }
	.table { border: 1px solid lightgray !important; }
</style>

<meta charset="UTF-8">

<!-- 부트스트랩 cdn -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<title>게시판 목록</title>
</head>
<body>
	<div class="container content-area">
		<div class="content-title">
			<h2 onclick="location.href='${contextPath}/list.bo?page=1';">게시판 목록</h2>
		</div>
		<div>
			<div class="table-responsive-xl" style="text-align: right;">
			  <div style="display: flex;" class="d-flex justify-content-end content-area-header">
			  	<input type="text" class="form-control" id="keyword" placeholder="글 제목을 입력하세요.">
			  	<button type="button" id="searchBtn" class="btn btn-primary btn-sm">검색</button>
	  			<a href="${contextPath}/regist.bo" class="btn btn-info btn-sm">글작성</a>
			  </div>
			  <table class="table" id="boardList">
			  	<thead class="table-active">
				  	<tr>
				  		<th>글번호</th>
				  		<th>제목</th>
				  		<th>조회수</th>
				  		<th>작성자</th>
				  		<th>작성일</th>
				  	</tr>
			  	</thead>
			  	
			  	<tbody>
			  		<c:choose>
			  			<c:when test="${empty list}">
			  				<tr>
					   			<td colspan="6" style="text-align: center;">존재하는 게시글이 없습니다.</td>
					   		</tr>
			  			</c:when>
			  			
			  			<c:otherwise>
			  				<c:forEach var="b" items="${list}">
			  					<tr>
						   			<td>${b.no}</td>
						   			<td>${b.title}</td>
						   			<td>${b.hit}</td>
						   			<td>${b.regId}</td>
						   			<td>${b.regDate}</td>
						   		</tr>
			  				</c:forEach>
			  			</c:otherwise>
			  		</c:choose>
			  	</tbody>
			  </table>
			  
			  <!-- 페이징 영역 -->
			   <ul class="pagination justify-content-center">
				  <c:choose>
				  	<c:when test="${page.currentPage eq 1}">
				  		<li class="page-item disabled"><a class="page-link" href="#">&lt;&lt;</a></li>
				  	</c:when>
				  	<c:otherwise>
				  		<li class="page-item"><a class="page-link" href="${contextPath}/list.bo?page=${page.currentPage -1}">&lt;&lt;</a></li>
				  	</c:otherwise>
				  </c:choose>
				  
				  <c:forEach var="p" begin="${page.startPage}" end="${page.endPage}">
					  <c:choose>
					  	<c:when test="${p eq page.currentPage}">
					  		<li class="page-item active"><a class="page-link" href="#">${p}</a></li>
					  	</c:when>
					  	<c:otherwise>
					  		<li class="page-item"><a class="page-link" href="${contextPath}/list.bo?page=${p}">${p}</a></li>
					  	</c:otherwise>
					  </c:choose>
				  </c:forEach>
				  
				  <c:choose>			  
					  <c:when test="${page.currentPage eq page.maxPage}">
					  	<li class="page-item disabled"><a class="page-link" href="#">&gt;&gt;</a></li>
					  </c:when>
					  <c:otherwise>
					  	<li class="page-item"><a class="page-link" href="${contextPath}/list.bo?page=${page.currentPage + 1}">&gt;&gt;</a></li>
					  </c:otherwise>
				  </c:choose>
			  </ul>
			</div>
		</div>
	</div>
	
	<!-- 
		alert 문구가 담겨있을 경우 -> alert 메시지 출력
					없을 경우 -> session에서 삭제
	
	 -->
	 
	 <c:if test="${sessionScope.alertMsg ne null}">
		<script>
	    	alert('${alertMsg}');
	    </script>
	</c:if>
	
	<c:remove var="alertMsg" scope="session" />
    
    
	<script>
		// 각 행 클릭시 상세 페이지 이동
		function tableRowClick(){
			$('#boardList>tbody>tr').on('click', function() {
	            location.href = '${contextPath}/detail.bo?no=' + $(this).children().eq(0).text();
	        });
		};
		
		$(function(){
			
			tableRowClick();
			
			// 검색 버튼 클릭시 ajax 
			$('#searchBtn').click(function(){
				
				if($('#keyword').val().trim() == '') {
					alert('검색어를 입력해주세요.');
					return;
				}
				
				$.ajax({
					url: '${contextPath}/search.bo',
					data: { page: 1, keyword: $('#keyword').val() },
					success: function(list){
						console.log(list);
						
						let value = '';
						
						if(list.length > 0){
							for(let i=0; i<list.length; i++){
					   			value += '<tr>' 
									  + '<td>' + list[i].no + '</td>'
									  + '<td>' + list[i].title + '</td>'
									  + '<td>' + list[i].hit + '</td>'
									  + '<td>' + list[i].regId + '</td>'
									  + '<td>' + list[i].regDate + '</td>'
									  + '</tr>';
							}
							
						}  else {				
							value += '<tr><td colspan="6" style="text-align: center;">존재하는 게시글이 없습니다.</td></tr>';
							$('.pagination').remove();
						}
						
						$('#boardList tbody').html(value);
						
					},
					error: function(){
						console.log('ajax 통신 실패');
					}
					
				});
			});
		});
	</script>
</body>
</html>
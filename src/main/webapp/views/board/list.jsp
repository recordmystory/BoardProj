<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="alertMsg" value="${requestScope.alertMsg}" />

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
	#boardList > thead > tr, #boardList > tbody > tr  { text-align: center; }
	.table { border: 1px solid lightgray !important; }
</style>

<meta charset="UTF-8">

<!-- 부트스트랩 cdn -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="../resources/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<title>게시판 목록</title>
</head>
<body>
	<div class="container content-area">
		<div class="content-title">
			<h2 onclick="location.href='/board/list.bo?page=1';">게시판 목록</h2>
		</div>
		<div>
			<div class="table-responsive-xl" style="text-align: right;">
			  <div style="display: flex;" class="d-flex justify-content-end content-area-header">
			  	<input type="text" class="form-control" id="keyword" placeholder="글 제목을 입력하세요.">
			  	<button type="button" id="searchBtn" class="btn btn-primary btn-sm">검색</button>
	  			<a href="/board/regist.bo" class="btn btn-info btn-sm">글작성</a>
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
				  		<li class="page-item disabled"><a class="page-link" href="#">&lt;</a></li>
				  	</c:when>
				  	<c:otherwise>
				  		<li class="page-item"><a class="page-link" href="/board/list.bo?page=${page.currentPage -1}">&lt;</a></li>
				  	</c:otherwise>
				  </c:choose>
				  
				  <c:forEach var="p" begin="${page.startPage}" end="${page.endPage}">
					  <c:choose>
					  	<c:when test="${p eq page.currentPage}">
					  		<li class="page-item active"><a class="page-link" href="#">${p}</a></li>
					  	</c:when>
					  	<c:otherwise>
					  		<li class="page-item"><a class="page-link" href="/board/list.bo?page=${p}">${p}</a></li>
					  	</c:otherwise>
					  </c:choose>
				  </c:forEach>
				  
				  <c:choose>			  
					  <c:when test="${page.currentPage eq page.maxPage}">
					  	<li class="page-item disabled"><a class="page-link" href="#">&gt;</a></li>
					  </c:when>
					  <c:otherwise>
					  	<li class="page-item"><a class="page-link" href="/board/list.bo?page=${page.currentPage + 1}">&gt;</a></li>
					  </c:otherwise>
				  </c:choose>
			  </ul>
			</div>
		</div>
	</div>
	
	<!-- 
		alert 문구가 담겨있을 경우 -> alert 메시지 출력
					없을 경우 -> request 영역에서 삭제
	
	 -->
	 
	 <c:if test="${requestScope.alertMsg ne null}">
		<script>
	    	alert('${alertMsg}');
	    </script>
	</c:if>
	
	<c:remove var="alertMsg" scope="request" />
    
    
	<script>
		// 각 행 클릭시 상세 페이지 이동
		function clickTableRow(){
			$('#boardList>tbody>tr').off('click');
			
			$('#boardList>tbody>tr').on('click', function() {
	            location.href = '/board/selectDetail.bo?no=' + $(this).children().eq(0).text();
	        });
		};
		
		 const monthMap = {
		           '1월': 'January',
		           '2월': 'February',
		           '3월': 'March',
		           '4월': 'April',
		           '5월': 'May',
		           '6월': 'June',
		           '7월': 'July',
		           '8월': 'August',
		           '9월': 'September',
		           '10월': 'October',
		           '11월': 'November',
		           '12월': 'December'
		       };
				 
	   // 월(한글) -> 월(영문)으로 변환
       function convertKorMonthToEng(month) {
           return monthMap[month];
       }
	   
       // 리터럴에서 월과 일을 추출해 Date 객체로 변환
       // 넘어오는 문자열 : ex) 7월 15일, 2024
       function parseDate(dateString) {
    	   var parts = dateString.split(' '); // ["7월", "15,", "2024"]
           var month = convertKorMonthToEng(parts[0]); // 7월 -> July로 변환
           var day = parseInt(parts[1].replace(',', '')) + 1; // 날짜에서 쉼표 제거 후 정수형으로 변환
           var year = parseInt(parts[2]); // 년도 정수형으로 변환
          
           var dateString = month + ' ' + day + ', ' + year; // Date형식으로 넣을 수 있게 변환
          
           var date = new Date(dateString);
           return date;
       }
       
	   $(function(){
		   
		   clickTableRow();
			
			$('#searchBtn').click(function(){
				
				if($('#keyword').val().trim() == '') {
					alert('검색어를 입력해주세요.');
					return;
				}
				
				// 검색 버튼 클릭시 ajax 
				$.ajax({
					url: '/board/listSearch.bo',
					data: { page: 1, keyword: $('#keyword').val() },
					success: function(resultMap){
						$('#boardList tbody').empty();
						
						let value = '';
						
						if(resultMap.list.length <= 0) {
							value += '<tr><td colspan="6" style="text-align: center;">존재하는 게시글이 없습니다.</td></tr>';
							$('.pagination').remove();
						}
						
						for(let i=0; i<resultMap.list.length; i++){
				            // YYYY-MM-DD 형식으로 변환
				            let formatRegDate = parseDate(resultMap.list[i].regDate).toISOString().split('T')[0];
							
				   			value += '<tr>' 
								  + '<td>' + resultMap.list[i].no + '</td>'
								  + '<td>' + resultMap.list[i].title + '</td>'
								  + '<td>' + resultMap.list[i].hit + '</td>'
								  + '<td>' + resultMap.list[i].regId + '</td>'
								  + '<td>' + formatRegDate + '</td>'
								  + '</tr>';
						}
						
						$('#boardList tbody').append(value);

						clickTableRow();
						$('.pagination').remove();
						
						/* // page 영역 다시 그리기
						let pageArea = '';
			            if (resultMap.page) {
			                let page = resultMap.page;
			                
			                // 이전 페이지 버튼
			                if(page.currentPage == 1){ // 현재 사용자가 보고 있는 페이지가 1페이지일때
			                    pageArea += '<li class="page-item disabled"><a class="page-link" href="#">&lt;</a></li>';
			                } else { // 현재 사용자가 보고 있는 페이지가 1페이지가 아닐 때
			                    pageArea += '<li class="page-item"><a class="page-link" href="/board/list.bo?page=' + (page.currentPage - 1) + '&keyword=' + resultMap.keyword + '">&lt;</a></li>';
			                }

			                for(let p = page.startPage; p <= page.endPage; p++) {
			                    if(p == page.currentPage){
			                        pageArea += '<li class="page-item active"><a class="page-link" href="#">' + p + '</a></li>';
			                    } else {
			                        pageArea += '<li class="page-item"><a class="page-link" href="/board/list.bo?page=' + p + '&keyword=' + resultMap.keyword + '">' + p + '</a></li>';
			                    }
			                }

			                // 다음 페이지 버튼
			                if(page.currentPage == page.maxPage){ // 현재 사용자가 보고 있는 페이지의 값과 가장 마지막 페이지의 값이 일치할 때
			                    pageArea += '<li class="page-item disabled"><a class="page-link" href="#">&gt;</a></li>';
			                } else {
			                    pageArea += '<li class="page-item"><a class="page-link" href="/board/list.bo?page=' + (page.currentPage + 1) + '&keyword=' + resultMap.keyword + '">&gt;</a></li>';
			                }
			            }
 */
			            
					},
					error: function(){
						alert('검색에 실패했습니다. 잠시후 다시 시도해주세요.');
					}
				});
			});
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

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
        min-height: 90%; 
        height: 750px;
        box-sizing: border-box; 
	}
	.content-title > h2 { font-weight: bold; text-align: center; margin: 15px 15px 15px 15px; }
	.btnArea { margin: 15px 15px 15px 15px; text-align: center; }
</style>

<meta charset="UTF-8">

<!-- 부트스트랩 cdn -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<title>글 작성</title>
</head>
<body>
	<div class="container content-area">
		<div class="content-title">
			<h2>글 작성</h2>
		</div>
		<form action="${contextPath}/insert.bo" method="post">
			<div>
				제목 <input type="text" class="form-control" name="title" style="width: 800px; margin-bottom: 15px;" value="${not empty sessionScope.enteredTitle ? sessionScope.enteredTitle : ''}" required>
				내용 <textarea class="form-control" name="content" style="white-space: pre; resize: none; height: 500px;" required>${not empty sessionScope.enteredContent ? sessionScope.enteredContent : ''}</textarea>
				<div class="btnArea">
					<button type="submit" class="btn btn-sm btn-primary">작성하기</button>			
					<button type="reset" class="btn btn-sm btn-secondary">초기화</button>			
					<button type="button" class="btn btn-sm btn-warning" onclick="history.back(-1);">뒤로가기</button>			
				</div>
			</div>
		</form>
	</div>
	
	<c:if test="${requestScope.alertMsg ne null}">
		<script>
	    	alert('${alertMsg}');
	    </script>
	</c:if>
	
	<c:remove var="alertMsg" scope="request" />
	<c:remove var="enteredTitle" scope="session" />
	<c:remove var="enteredContent" scope="session"/>
</body>
</html>
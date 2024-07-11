<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<!-- 부트스트랩 cdn -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<style>
	.content-area { 
		border: 1px solid black;
		border-radius: 10px;
        margin: 10px auto;
        padding: 10px;
        max-width: 80%; 
        height: 700px;
        box-sizing: border-box; 
        display: flex;
        flex-direction: column;
	}
	.content-title > h1 { font-weight: bold; text-align: center; margin: 15px 15px 15px 15px; }
	.btn-area { text-align: center; margin-top: 30px; }

</style>

<title>게시판 서비스</title>
</head>
<body>
		<div class="container content-area">
			<div class="content-title" style="height: 200px;">
				<h1>게시판 서비스</h1>
			</div>
			<div class="btn-area" style="height: 500px; padding: 160px 0px 160px 0px;">
				<a href="${contextPath}/list.bo?page=1" class="btn btn-primary btn-lg">게시판 목록</a>	
			</div>
		</div>
	
</body>
</html>
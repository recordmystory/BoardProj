<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.jw.board.model.vo.Board"%>
<% Board b = (Board) request.getAttribute("b"); %>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>

<style>
	.content-area { 
		border: 1px solid black;
		border-radius: 10px;
        margin: 10px auto;
        padding: 10px;
        max-width: 80%;
        min-height: 90%; 
        height: 800px;
        box-sizing: border-box; 
	}
	.content-title > h2 { font-weight: bold; text-align: center; margin: 15px 15px 15px 15px; }
	.btnArea { margin: 30px 15px 15px 15px; text-align: center; }
</style>

<!-- 부트스트랩 cdn -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<div class="container content-area">
		<div class="content-title">
			<h2>게시글 수정</h2>
		</div>
		<form action="/bbs/update.bo?no=${b.no}" method="post">
			<div class="table-area">
				<table class="table">
					<tr>
						<th>제목</th>
						<td><input type="text" class="form-control" value="${b.title}" name="title" style="width: 800px;" required></td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<textarea class="form-control" rows="10" cols="5" name="content" style="white-space: pre; resize: none; height: 500px;" required>${b.content}</textarea>
						</td>
					</tr>
					<tr>
						<th>작성일</th>
						<td>${b.regDate}</td>
					</tr>
				</table>
				<div class="btnArea">
					<button type="submit" class="btn btn-sm btn-primary">수정하기</button>			
					<button type="button" class="btn btn-sm btn-warning" onclick="history.back(-1);">뒤로가기</button>			
				</div>
			</div>
		</form>
	</div>
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
<meta charset="UTF-8">
<style>
	body{margin-top:20px;}
	.error-page {
	    text-align: center;
		background: #fff;
		border-top: 1px solid #eee;
	}
	.error-page .error-inner {
		display: inline-block;
	}
	.error-page .error-inner h1 {
		font-size: 140px;
		text-shadow: 3px 5px 2px #3333;
		color: #006DFE;
		font-weight: 700;
	}
	.error-page .error-inner h1 span {
		display: block;
		font-size: 25px;
		color: #333;
		font-weight: 600;
		text-shadow: none;
	}
	.error-page .error-inner p {
		padding: 20px 15px;
	}
	.error-page .search-form {
		width: 100%;
		position: relative;
	}
	.error-page .search-form input {
		width: 400px;
		height: 50px;
		padding: 0px 78px 0 30px;
		border: none;
		background: #f6f6f6;
		border-radius: 5px;
		display: inline-block;
		margin-right: 10px;
		font-weight:400;
		font-size:14px;
	}
	.error-page .search-form input:hover{
		padding-left:35px;
	}
	.error-page .search-form .btn {
		width: 80px;
		height: 50px;
		border-radius: 5px;
		cursor: pointer;
		background: #006DFE;
		display: inline-block;
		position: relative;
		top: -2px;
	}
	.error-page .search-form .btn i{
		font-size:16px;
	}
	.error-page .search-form .btn:hover{
		background:#333;
	}
</style>
<title>게시판 서비스</title>
</head>
<body>	
	<section class="error-page section">
		<div class="container">
			<div class="row">
				<div class="col-lg-6 offset-lg-3 col-12">
					<!-- Error Inner -->
					<div class="error-inner">
						<h1>처리 실패<span>${sessionScope.alertMsg}</span></h1>
					</div>
					<!--/ End Error Inner -->
				</div>
			</div>
		</div>
	</section>
	
<%-- 	<c:if test="${sessionScope.alertMsg ne null}">
		<script>
	    	alert('${alertMsg}');
	    </script>
	</c:if>
	
	<c:remove var="alertMsg" scope="session" />
 --%>	
</body>
</html>
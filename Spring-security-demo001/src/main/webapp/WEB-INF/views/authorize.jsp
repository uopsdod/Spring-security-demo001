<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>
<html lang="en">
<head>

<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.2.0/css/bootstrap.min.css" />
<script src="/webjars/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<!-- 
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />

</head>
<body>

	<nav class="navbar navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Spring Boot</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#about">About</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">

		<div class="starter-template">
			<h1>Spring Boot Web JSP Example</h1>
			<h3>Notify the Authorization server: this OAuth client(third-party website) wants to access my resource (usually the third-party website will do this) </h3>
			<h3>Resource owner logins in the Authorization server</h3>
			<h3>Authorization server will then ask Resource owner: Do you really want to allow/deny its access? (then, you'll be asked for permission) </h3>
			
<%-- 			<h2>Message: ${message}</h2> --%>
		</div>
		
		<!-- OAuth Client will redirect Resource owner's request to Authorization server -->
		<!-- Authorization server than asks Resource owner whether to allow this OAuth client to access your protected resources -->
		
		<form action="/oauth/authorize" method="get" target="_blank" role="form" class="form-horizontal">
			ClientId: <input type="text" name="client_id" value="oauth_client"><br> 
			Scope: <input type="text" name="scope" value="read"><br>
			Response type: <input type="text" name="response_type" value="code"><br>
			State: <input type="text" name="state" value="rensanning"><br>
			<button type="submit">Submit</button>
<!-- 			<button type="submit" formmethod="post">Submit using POST</button> -->
		</form>

	</div>
	<!-- /.container -->

	<script type="text/javascript"
		src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>

</html>

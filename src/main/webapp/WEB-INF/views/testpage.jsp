<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.text.NumberFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.io.*,java.util.*" %>
<%java.text.DateFormat dft = new java.text.SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a "); %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Title</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
    <script>
    window.history.forward();
    </script>
       
  </head>
  <body>
	<header style="background-color: #ff6600;">		
			<div class="navigation">
				<div class="container">					
					<div class="navbar-header">
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse.collapse">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>						
					</div>

								<form class="navbar-form navbar-right" role="search"
									action="lookcity" method="post">
									<div class="input-group add-on">
										<input type="hidden" name="id" value="${attribs.id}" /> <input
											type="text" class="form-control"
											placeholder="Look up City" name="lookcity"
											id="lookcity" SIZE='20'>
										<div class="input-group-btn">
											<button class="btn btn-default" type="submit">
												<i class="glyphicon glyphicon-search"></i>
											</button>
										</div>
									</div>
								</form>
					
					<div class="navbar-collapse collapse">							
						<div class="menu">
							<ul class="nav nav-tabs" role="tablist">
								<li ><a href="index">Home</a></li>
								<li><a href="about">About Us</a></li>																
								<li><a href="contact">Contact Us</a></li>	
								<li><a href="signups">Sign up</a></li>
								<li><a href="login">login</a></li>
								<li><a href="services">Look-Ups</a></li>
								<li class="active"><a href="testpage">Test Page</a></li>				
							</ul>
						</div>
					</div>						
				</div>
			</div>				
	</header>
	
	<div class="container">

		<div>
			<h1 style="color: #008000">Welcome to the Gettin' Hitched Website</h1>
			<h3 style="color: #008000">${msg}
				<br> ${msg2}
			</h3>
			<h2 style="color: #008000">${message}</h2>
			<h2 style="color: #008000">${success}</h2>
			<h2 style="color: #FF0000">${error}</h2>

			<form id="myForm" action="testpost" method="post" class="bs-example form-horizontal">
				<fieldset>
					<legend class = "text-center">Gettin' Hitched test </legend>
					<br>

					<div class="form-group">
						<label for="userNameInput" class="col-lg-2 control-label">Enter your info?</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="emailAddress"
								id="emailAddress" placeholder="What is my email address" />
							<br>
							<input type="text" class="form-control" name="fname"
								id="fname" placeholder="What is my Name" />
								
						</div>

						<button class="btn btn-primary">Submit</button>

						<!-- <a href="index" class="btn btn-primary" role="button">Cancel</a> <a
							href="forgotpassword" class="btn btn-primary" role="button">Forgot
							Password</a> -->
					</div>
				</fieldset>
			</form>
<div class="col-md-3" >
			<div class="pull-center" style="background-color: lightgray">
				<div class="text-info">
					<h3>${success}<br> Club Locations and <br>Contact
						information <br> __________________
					</h3>
				</div>

				<c:forEach items="${addresses}" var="item">
					<div>
						<c:out value="${item.bname}" />
						</a> <br>
						<c:out value="${item.address}" />
						<br>
						<c:out value="${item.city}, " />
						<c:out value="${item.state} " />
						<c:out value="${item.zip}" />
						<br>
						<c:out value="${item.tel}" />
					</div>
					<br>
				</c:forEach>
			</div>
</div>


		</div>
		<div></div>
	</div>
   
	<footer  style="background-color: #ff6600; position: fixed; bottom:0;width:100%;" >						
				<div class="col-md-5 col-md-offset-5">
				<div class="menu">
							<ul class="nav nav-tabs" role="tablist">
								<li ><a href="index">Home</a></li>
								<li><a href="about">About Us</a></li>																
								<li><a href="contact">Contact Us</a></li>	
								<li><a href="signups">Sign up</a></li>
								<li><a href="login">login</a></li>	
								<li><a href="services">Look-Ups</a></li>							
								<li class="active"><a href="testpage">Test Page</a></li>				
						        
							</ul>
							
						</div>
						</div>
		
	</footer>
  </body>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 	 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	
</html>

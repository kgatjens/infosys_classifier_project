<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<h:body>
	
		<h1>Rserve and JSF</h1>
	 
		<h:form>
			<p:graphicImage value="#{plotr.plotRImage}" />
		</h:form>
	
		<c:set var="indx" value="#{plotr.messageResponse}" scope="request"></c:set>	
	
	</h:body>

</body>
</html>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<html>
<head>
    <link rel="icon" href="data:;base64,=">
</head>
<body>
<h1>欢迎登录xxx系统！</h1><br/>
<h1>用户信息</h1><br/>
<h2><%request.getAttribute("username");%></h2>
    <h2>我是服务器：${pageContext.request.localPort}</h2>
    <h2>当前sessionId：${pageContext.session.id}</h2>
</body>
</html>

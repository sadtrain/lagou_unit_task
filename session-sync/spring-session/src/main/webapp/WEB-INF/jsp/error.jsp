<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<html>
<head>
    <title>登录失败</title>

    <style>
        div{
            width:300px;
            height:100px;
            position: absolute;
            top:50%;
            left:50%;
            margin-top: -50px ;
            margin-left:-150px;
        }
    </style>
</head>
<body>
<h1>登录失败！</h1><br/>
<h2>我是服务器：${pageContext.request.localPort}</h2>
<h2>当前sessionId：${pageContext.session.id}</h2>
</body>
</body>
</html>

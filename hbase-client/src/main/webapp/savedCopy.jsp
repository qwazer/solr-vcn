<%--
  User: ar
  Date: 1/29/14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="ru.qwazer.hbase.client.webapp.HbaseUtil" %>
<html>
<head>
    <title>Saved copy of page <%= HbaseUtil.findUrl(request.getParameter("rawId")) %></title>
</head>
<body>
<div id="head">
    This is saved copy of page <%= HbaseUtil.findUrl(request.getParameter("rawId")) %>  <br/> Datetime is <%=HbaseUtil.findDate(request.getParameter("rawId")) %>
</div>
<br/>
<hr/>

<iframe width="100%" height="100%" frameborder="0" src="savedContent.jsp?rawId=<%= request.getParameter("rawId") %>"/>


</body>
</html>



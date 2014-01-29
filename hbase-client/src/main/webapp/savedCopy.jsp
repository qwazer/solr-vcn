<%--
  User: ar
  Date: 1/29/14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="ru.qwazer.hbase.client.webapp.HbaseUtil" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="head">

    This is saved copy of page <%= HbaseUtil.findUrl(request.getParameter("rawId")) %>  <br/> Datetime is <%=HbaseUtil.findDate(request.getParameter("rawId")) %>
</div>
<br/>
<hr/>

<%--<jsp:include page="/savedContent.jsp?rawId=<%= request.getParameter("rawId") %>"/>--%>
<jsp:include page="/savedContent.jsp"/>


</body>
</html>

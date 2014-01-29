<%--
  User: ar
  Date: 1/29/14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="ru.qwazer.hbase.client.webapp.HbaseUtil" %>

<%= HbaseUtil.findContent(request.getParameter("rawId")) %>

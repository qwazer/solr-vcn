<%--
  User: ar
  Date: 1/29/14
--%>

<%@ page import="ru.qwazer.hbase.client.webapp.HbaseUtil" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>

<%
    OutputStream o = response.getOutputStream();
    InputStream is = HbaseUtil.findContent(request.getParameter("rawId"));
    byte[] buf = new byte[32 * 1024]; // 32k buffer
    int nRead = 0;
    while ((nRead = is.read(buf)) != -1) {
        o.write(buf, 0, nRead);
    }
    o.flush();
    o.close();// *important* to ensure no more jsp output
%>

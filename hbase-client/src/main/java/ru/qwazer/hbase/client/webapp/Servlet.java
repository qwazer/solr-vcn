package ru.qwazer.hbase.client.webapp;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.util.Bytes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ar
 * @since Date: 30.01.2014
 */
public class Servlet extends HttpServlet {

    private String headOpen = "<html>\n" +
            "<head>\n" +
            "    <title>Saved copy of page ";

    private String headTitle = "</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"head\" style=\"color:#000;font-size:12pt;margin:5;padding:5;font-family:sans\">\n" +
            "    This is saved copy of page ";

    private String headClose = "</div>\n" +
            "<br/>\n" +
            "<hr/>";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        OutputStream o = response.getOutputStream();

        String rawId = request.getParameter("rawId");
        if (rawId == null) {
            o.write(Bytes.toBytes("Provide rawId parameter"));
            return;
        }

        String p = request.getParameter("p");
        if (rawId == null) {
            o.write(Bytes.toBytes("Provide p parameter"));
            return;
        }

        if (!DigestUtils.md5Hex(rawId).equals(p)) {
            o.write(Bytes.toBytes("rawId don't match p parameter, do you try to access directly?"));
            return;
        }

        checkMIMEAndWriteHeader(request, response, o, rawId);

        InputStream is = HbaseUtil.findContent(request.getParameter("rawId"));
        byte[] buf = new byte[32 * 1024]; // 32k buffer
        int nRead = 0;
        while ((nRead = is.read(buf)) != -1) {
            o.write(buf, 0, nRead);
        }

    }

    private void checkMIMEAndWriteHeader(HttpServletRequest request, HttpServletResponse response, OutputStream o, String rawId) throws IOException {
        if (rawId.endsWith(".pdf")) {
            response.addHeader("Content-Type", "application/pdf");
        } else if (rawId.endsWith(".doc") || rawId.endsWith(".docx")) {
            response.addHeader("Content-Type", "application/msword");
        } else if (rawId.endsWith(".xls") || rawId.endsWith(".xlsx")) {
            response.addHeader("Content-Type", "application/vnd.ms-excel");
        } else {
            writeHeader(request, o);
        }
    }

    private void writeHeader(HttpServletRequest request, OutputStream o) throws IOException {
        o.write(Bytes.toBytes(headOpen));

        String url = HbaseUtil.findUrl(request.getParameter("rawId"));
        o.write(Bytes.toBytes(url));
        o.write(Bytes.toBytes(headTitle));

        String headInfo = url + "  <br/> Datetime is " + HbaseUtil.findDate(request.getParameter("rawId"));
        o.write(Bytes.toBytes(headInfo));
        o.write(Bytes.toBytes(headClose));
    }
}

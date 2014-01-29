package ru.qwazer.solr.hbase.access;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author ar
 * @since Date: 28.01.2014
 */
public class HbaseAccessor {

    public static StringWriter getHTML(String id){
        StringWriter stringWriter =  new StringWriter();
        stringWriter.write("Hello dummy string" + id);   //todo
        return stringWriter;
    }


    public static StringWriter getURL(String id){
        StringWriter stringWriter =  new StringWriter();
        stringWriter.write("Hello dummy string" + id);   //todo
        return stringWriter;
    }




    public static String getContent(String rowId) throws IOException {  //todo StringWriter
        StringWriter stringWriter = new StringWriter();
        Configuration conf = HBaseConfiguration.create();
        conf.addResource(new Path("/home/hadoop/hbase-0.90.4/conf/hbase-site.xml"));

        HTable table = new HTable(conf, "AZCrawl_webpage");

        Get get = new Get(Bytes.toBytes(rowId));
        get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("cnt"));

        Result result = table.get(get);
        return  Bytes.toString(result.value());

    }

}

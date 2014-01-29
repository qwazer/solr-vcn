package ru.qwazer.hbase.client.webapp;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.avro.generated.HBase;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author ar
 * @since Date: 29.01.2014
 */
public class HbaseUtil {


    static {
        loadProperties();
        tableNames = findTableNames();
    }

    static Properties prop;
    static List<String> tableNames;

    private static void loadProperties() {
        prop = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("hbase-conf.properties");
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<String> findTableNames() {
        if (prop.getProperty("defaultTable") != null) {
            List<String> list = new ArrayList<String>();
            list.add(prop.getProperty("defaultTable"));
            return list;
        } else return findHbaseTableNames();

    }


    private static List<String> findHbaseTableNames() {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(prop.getProperty("hbaseConfig"));

        HBaseAdmin hBaseAdmin = null;
        HTableDescriptor[] tables = null;
        try {
            hBaseAdmin = new HBaseAdmin(conf);
            tables = hBaseAdmin.listTables();
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> list = new ArrayList<String>();
        if (tables != null) {
            for (int i = 0; i < tables.length; i++) {
                list.add(tables[i].getNameAsString());
            }
        }
        return list;
    }


    public static String findContent(String rowId) throws IOException {

        return retriveValue(rowId, "f", "cnt");

    }

    public static String findUrl(String rowId) throws IOException {
        return retriveValue(rowId, "f", "bas");
    }

    public static Date findDate(String rowId) throws IOException {
        return retriveDate(rowId, "f", "cnt");
    }


    private static String retriveValue(String rowId, String family, String column) throws IOException {

        for (String tableName : tableNames) {
            Result result = retrieveResult(tableName, rowId, family, column);
            if (result.value() != null)
                return Bytes.toString(result.value());
        }
        return null;
    }

    private static Date retriveDate(String rowId, String family, String column) throws IOException {
        for (String tableName : tableNames) {
            Result result = retrieveResult(tableName, rowId, family, column);
            if (result.value() != null) {
                long timestamp = 0;
                if (result.raw().length > 0) {
                    timestamp = result.raw()[0].getTimestamp();
                }
                return new Date(timestamp);
            }
        }
        return null;

    }


    private static Result retrieveResult(String tableName, String rowId, String family, String column) throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(prop.getProperty("hbaseConfig"));

        HTable table = new HTable(conf, tableName);

        Get get = new Get(Bytes.toBytes(rowId));
        get.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));

        return table.get(get);
    }

}

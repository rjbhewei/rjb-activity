package com.hewei.utils;

import com.hewei.common.constants.CommonConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 
 * @author hewei
 * 
 * @date 2015/10/22  21:26
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class HbaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(HbaseUtils.class);

    static Connection connection;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", CommonConstants.HBASE_ZOOKEEPER_QUORUM);
        conf.set("hbase.zookeeper.property.clientPort", String.valueOf(CommonConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT));
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(TableName tableName, String... columnFamilys) throws Exception {
        Admin admin = connection.getAdmin();
        if (admin.tableExists(tableName)) {
            logger.info("hbase table is exist");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (String columnFamily : columnFamilys) {
                tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            }
            admin.createTable(tableDesc);
            logger.info("create table success");
        }
    }

    public static void addRow(TableName tableName, String row, String columnFamily, String column, String value) throws Exception {
        Table table = connection.getTable(tableName);
        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));// 参数出分别：列族、列、值
        table.put(put);
        logger.info("add row success");
    }

}

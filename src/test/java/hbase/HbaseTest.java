package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hewei
 * 
 * @date 2015/10/21  18:06
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class HbaseTest {

    private static final Logger logger = LoggerFactory.getLogger(HbaseTest.class);

    static Connection connection;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "172.18.2.39,172.18.2.40,172.18.2.121");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(TableName tableName, String[] columnFamilys) throws Exception {
        Admin admin = connection.getAdmin();
        if (admin.tableExists(tableName)) {
            logger.info("表已经存在");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (String columnFamily : columnFamilys) {
                tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            }
            admin.createTable(tableDesc);
            logger.info("创建表成功");
        }
    }

    public static void deleteTable(TableName tableName) throws Exception {
        Admin admin = connection.getAdmin();
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);// 关闭一个表
            admin.deleteTable(tableName); // 删除一个表
            logger.info("删除表成功");
        } else {
            logger.info("删除的表不存在");
        }
    }

    public static void addRow(TableName tableName, String row, String columnFamily, String column, String value) throws Exception {
        Table table = connection.getTable(tableName);
        Put put = new Put(Bytes.toBytes(row));
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));// 参数出分别：列族、列、值
        table.put(put);
        logger.info("添加行成功");
    }

    public static void delRow(TableName tableName, String row) throws Exception {
        Table table = connection.getTable(tableName);
        Delete del = new Delete(Bytes.toBytes(row));
        table.delete(del);
        logger.info("删除行成功");
    }

    public static void delMultiRows(TableName tableName, String... rows) throws Exception {
        Table table = connection.getTable(tableName);
        List<Delete> list = new ArrayList<>();
        for (String row : rows) {
            Delete del = new Delete(Bytes.toBytes(row));
            list.add(del);
        }
        table.delete(list);
        logger.info("删除多行成功");
    }

    public static void getRow(TableName tableName, String row) throws Exception {
        Table table = connection.getTable(tableName);
        Get get = new Get(Bytes.toBytes(row));
        Result result = table.get(get);
        for (Cell cell : result.rawCells()) {
            logger.info("Row Name: {}", new String(CellUtil.cloneRow(cell)));
            logger.info("Timestamp: {}", cell.getTimestamp());
            logger.info("column Family: {}", new String(CellUtil.cloneFamily(cell)));
            logger.info("Row Name:  {}", new String(CellUtil.cloneQualifier(cell)));
            logger.info("Value: {}", new String(CellUtil.cloneValue(cell)));
        }
    }

    public static void getAllRows(TableName tableName) throws Exception {
        Table table = connection.getTable(tableName);
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        // 输出结果
        for (Result result : results) {
            for (Cell cell : result.rawCells()) {
                logger.info("Row Name: {}", new String(CellUtil.cloneRow(cell)));
                logger.info("Timestamp: {}", cell.getTimestamp());
                logger.info("column Family: {}", new String(CellUtil.cloneFamily(cell)));
                logger.info("Row Name:  {}", new String(CellUtil.cloneQualifier(cell)));
                logger.info("Value: {}", new String(CellUtil.cloneValue(cell)));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TableName tableName = TableName.valueOf("test.hbase");
        String[] familyNames = new String[]{"host", "url", "text"};
        String row = "hewei";
        //        deleteTable(tableName);
        createTable(tableName, familyNames);
        addRow(tableName, row, "host", "shuyun", "hostvalue" + System.currentTimeMillis() / 1000);
        //        getRow(tableName, row);
        getAllRows(tableName);
        //        delRow(tableName, "hewei");
        //        delMultiRows(tableName, "hewei");

        //        deleteTable(tableName);

    }

}

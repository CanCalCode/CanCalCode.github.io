package com.lyhstu.mhl.dao;

import com.lyhstu.mhl.utils.JDBCUtilsByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//本类封装了DBUtils工具的QueryRunner类和Druid连接池,简化和服务器的命令传输和接收代码
public class BasicDAO<T> {
    private final QueryRunner qr = new QueryRunner();

    //开发通用的dml方法, 针对任意的表
    public int update(String sql, Object... parameters) {//执行增删改功能
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.update(connection,sql, parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            //因为result,preparedStatement已经在Apache-DBUtils->queryRunner()中关闭了,所以这里只需要关闭connection就行了
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }
    /**
     * @param sql sql 语句，可以有 ?
     * @param clazz 传入一个类的Class对象 比如 Actor.class
     * @param parameters 传入 ? 的具体的值，可以是多个
     * @return 根据Actor.class 返回对应的 ArrayList 集合
     */
    public List<T> queryMulti(String sql,Class<T> clazz,Object...parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            List<T> query = qr.query(connection, sql, new BeanListHandler<>(clazz), parameters);
            return query;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            //因为resultSet,preparedStatement已经在Apache-DBUtils->queryRunner()中关闭了,所以这里只需要关闭connection就行了
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //查询单行的方法
    public T querySingle(String sql,Class<T> clazz,Object...parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            T result = qr.query(connection, sql, new BeanHandler<>(clazz), parameters);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            //因为resultSet,preparedStatement已经在Apache-DBUtils->queryRunner()中关闭了,所以这里只需要关闭connection就行了
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }


    //返回单个数据元素,即单值
    public Object queryScalar(String sql, Object... parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            Object obj = qr.query(connection, sql, new ScalarHandler(), parameters);
            return obj;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            //因为resultSet,preparedStatement已经在Apache-DBUtils->queryRunner()中关闭了,所以这里只需要关闭connection就行了
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }
}






















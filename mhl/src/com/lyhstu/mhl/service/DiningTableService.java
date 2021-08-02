package com.lyhstu.mhl.service;

import com.lyhstu.mhl.dao.DiningTableDAO;
import com.lyhstu.mhl.domain.DiningTable;

import java.util.List;

public class DiningTableService {
    private final DiningTableDAO diningDao = new DiningTableDAO();//执行sql语句的DBUtils对象

    /**
     * 获得餐桌状态
     * @return 返回餐桌对象集合(包括餐桌状态)
     */
    public List<DiningTable> getDiningState() {
        String sql = "select * from diningTable";
        return diningDao.queryMulti(sql, DiningTable.class);
    }
    /**
     * 设置餐桌的状态
     * @param tableId 餐桌号
     * @param state 要设置的状态
     * @return 返回boolean
     */
    public boolean setTableState(Integer tableId,String state) {
        String sql = "update diningTable set state = ? where id = ?";
        int update = diningDao.update(sql, state, tableId);
        return update > 0;
    }

    /**
     * 预定餐桌
     * @param tableId 餐桌号
     * @param orderName 预定人名字
     * @param orderTel 预订人电话
     * @return 返回是否预定成功
     */
    public boolean orderTable(Integer tableId, String orderName, String orderTel) {
        String sql = "update diningTable set orderName = ?,orderTel = ?,state = ? where id = ? and state = '空'";
        int rows = diningDao.update(sql,orderName, orderTel, "已预定",tableId);
        return rows > 0;
    }
    /**
     * 判断桌号是否存在
     * @param tableId 需要判断的桌号
     * @return 返回结果
     */
    public boolean isTableExist(int tableId) {
        String sql = "select * from diningTable where id = ?";
        DiningTable diningTable = diningDao.querySingle(sql, DiningTable.class, tableId);
        return diningTable != null;
    }

    /**
     * 判断餐桌是否被预定
     * @param tableId 餐桌号
     * @return 返回结果
     */
    public boolean isTableOrdered(int tableId) {
        String sql = "select state from diningTable where id = ?";
        String obj = (String)diningDao.queryScalar(sql,tableId);
        return  "已预定".equals(obj);
    }

    /**
     * 判断餐桌是否已点餐
     * @param tableId 餐桌号
     * @return 返回结果
     */
    public boolean isTableBilled(int tableId) {
        String sql = "select state from diningTable where id = ?";
        String obj = (String)diningDao.queryScalar(sql,tableId);
        return  "已点餐".equals(obj);
    }

    /**
     * 判断餐桌是否为'空'
     * @param tableId 餐桌号
     * @return 返回结果
     */
    public boolean isTableNull(int tableId) {
        String sql = "select state from diningTable where id = ?";
        String obj = (String)diningDao.queryScalar(sql,tableId);
        return  "空".equals(obj);
    }
    /**
     * 清空一个桌子的资料并返回boolean
     * @param tableId 餐桌号
     * @return 返回boolean
     */
    public boolean clearTable(int tableId) {
        String sql = "UPDATE diningTable SET state = '空',orderName = '',orderTel = '' WHERE id = ?";
        int update = diningDao.update(sql, tableId);
        return update > 0;
    }
}

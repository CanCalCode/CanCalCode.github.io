package com.lyhstu.mhl.service;

import com.lyhstu.mhl.dao.BillDAO;
import com.lyhstu.mhl.domain.Bill;
import com.lyhstu.mhl.domain.Menu;
import java.util.List;
import java.util.UUID;

public class BillService {
    private final BillDAO billDAO = new BillDAO();
    private final MenuService menuService = new MenuService();
    private final DiningTableService diningService = new DiningTableService();

    /**
     * 点餐功能
     *根据传入的桌号,菜品,数量在bill中插入一条数据,然后如果插入成功则修改餐桌状态为"已点餐"
     * @param tableId 点餐桌号
     * @param menuId  菜品名称
     * @param nums    菜品数量
     * @return 返回是否点餐成功
     */
    public boolean orderBill(Integer tableId, Integer menuId, Integer nums) {
        String sql = "insert into bill values(NULL,?,?,?,?,?,now(),'未结账','空')";
        Menu menu = menuService.getMenu(menuId);//菜品对象应该不是null,因为传入之前已经判断过了
        double totalMoney = menu.getPrice() * nums;//本账单总金额
        String billId = UUID.randomUUID().toString();//UUID可以生成一个非重复的字符串,可以作为账单号
        int update = billDAO.update(sql, billId, menuId, nums, totalMoney, tableId);//插入数据
        if (update > 0) {
            diningService.setTableState(tableId, "已点餐");//修改餐桌状态为"已点餐"
        }
        return update > 0;
    }

    /**
     * 返回所有账单
     * 返回所有bill表所有账单
     * @return 一Bill对象集合的形式返回所有账单(包括结账和未结账的)
     */
    public List<Bill> getAllBill() {
        String sql = "select * from bill";
        List<Bill> bills = billDAO.queryMulti(sql, Bill.class);
        return bills;
    }

    /**
     * 返回未结账账单
     * 返回指定桌号的未结账的账单
     * @param tableId 指定的桌号
     * @return 返回指定桌号的账单
     */
    public List<Bill> getUncheckedBill(int tableId) {
        System.out.println("======");
        String sql = "select * from bill where diningTableId = ? and state = '未结账'";
        List<Bill> bills = billDAO.queryMulti(sql, Bill.class,tableId);
        return bills;
    }

    /**
     * 返回该桌总费用
     * @param tableId 桌号
     * @return 返回总费用
     */
    public double getTotalMoney(int tableId) {
        String sql = "SELECT sum(money) FROM bill WHERE diningTableId = ? AND state = '未结账'";
        Object obj = billDAO.queryScalar(sql, tableId);
        return (double) obj;
    }

    /**
     * 结账
     * 根据桌号设置账单状态和支付方式
     * @param tableId 桌号
     * @param state 账单状态
     * @param payType 支付方式
     * @return 返回执行结果 boolean
     */
    public boolean setBillState(int tableId,String state,String payType) {
        String sql = "update bill set state = ?,payType = ? where diningTableId = ? and state = ?";
        int update = billDAO.update(sql,state,payType,tableId,"未结账");
        boolean b = false;
        if (update > 0) {
            b = diningService.clearTable(tableId);//清空此餐桌
        }
        return b;
    }
}

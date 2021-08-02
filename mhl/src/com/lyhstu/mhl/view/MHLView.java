package com.lyhstu.mhl.view;

import com.lyhstu.mhl.domain.Bill;
import com.lyhstu.mhl.domain.DiningTable;
import com.lyhstu.mhl.domain.Employee;
import com.lyhstu.mhl.domain.Menu;
import com.lyhstu.mhl.service.BillService;
import com.lyhstu.mhl.service.DiningTableService;
import com.lyhstu.mhl.service.EmployeeService;
import com.lyhstu.mhl.service.MenuService;
import com.lyhstu.mhl.utils.Utility;
import java.util.List;

//这是主界面
public class MHLView {
    private boolean loop = true;
    private final EmployeeService empService = new EmployeeService();
    private final DiningTableService diningService = new DiningTableService();
    private final MenuService menuService = new MenuService();
    private final BillService billService = new BillService();

    public void mainMenu() {
        while (loop) {//进入一级菜单
            System.out.println("=============满汉楼菜单系统==============");
            System.out.println("\t\t"+"1 登录满汉楼");
            System.out.println("\t\t"+"2 退出满汉楼");
            System.out.print("请输入您的选择:");
            String key = Utility.readString(1);
            switch (key) {//进入二级菜单
                case "1" -> {
                    System.out.print("请输入员工号:");
                    String empId = Utility.readString(50);
                    System.out.print("请输入密码:");
                    String password = Utility.readString(50);
                    Employee e = empService.getEmployeeByIdAndPwd(empId, password);
                    if (e != null) {//e!=null 验证通过,登录成功
                        while (loop) {
                            System.out.println("============登录成功["+e.getName()+"]============\n");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 查看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.print("请输入您的选择:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1" -> showTableState();//1,显示餐桌状态
                                case "2" -> orderTable();//2,预定餐桌
                                case "3" -> showMenus();//3,显示所有菜品
                                case "4" -> orderMenus(); //4,点餐服务
                                case "5" -> showBills();//5,查看账单
                                case "6" -> payBill(); //6,结账
                                case "9" -> loop = false;//退出系统
                                default -> System.out.println("输入错误,请重新输入\n");
                            }
                        }
                    } else {
                        System.out.println("==============登录失败!==============");
                    }
                }
                case "2" -> loop = false;
                default -> System.out.println("输入错误,请重新输入\n");
            }
        }
        System.out.println("退出满汉楼系统");
    }

    //1 显示餐桌状态
    public void showTableState() {
        System.out.println("餐桌号\t\t餐桌状态\t\t预订人\t\t预定人电话");
        for (DiningTable d : diningService.getDiningState()) {
            System.out.println(d);
        }
    }
    // 2 预定餐桌
    public void orderTable() {
        System.out.print("请输入需要预定的餐桌号:");
        int tableId = Utility.readInt();
        if (!diningService.isTableExist(tableId)) {//判断输入的餐桌号是否存在
            System.out.println("您输入的餐桌号有误,请重新预定");
            return;
        }
        System.out.print("请输入预订人姓名:");
        String orderName = Utility.readString(50);
        System.out.print("请输入预订人手机号:");
        String orderTel = Utility.readString(20);
        boolean b = diningService.orderTable(tableId, orderName, orderTel);//返回是否预定成功
        if (b) {//预定成功
            System.out.println(orderName + " 先生/女士 预定成功" + tableId + "号桌! 联系电话:" + orderTel);
        } else {//预定失败
            System.out.println("抱歉,预定失败,餐桌已经被预定或正在就餐中,请选择其他餐桌");
        }
    }
    // 3 显示所有菜品
    public void showMenus() {
        System.out.println("菜品编号\t\t菜品名\t\t类别\t\t价格");
        for (Menu menu:menuService.getMenuList()) {
            System.out.println(menu);
        }
    }
    // 4 点餐服务
    public void orderMenus() {
        //1,输入桌号
        int tableId;
        while (true) {
            System.out.print("请输入点餐桌号(-1退出):");
            tableId = Utility.readInt();
            if (tableId == -1) {
                return;
            }
            if (!diningService.isTableExist(tableId)) {//判断桌号是否存在
                System.out.print("您输入的餐桌号有误,请重新输入.\n");
                continue;
            }
            //判断此桌号是否既不是预定状态又不是已点餐状态,如果都不是则此桌为空,不能点餐
            if (!diningService.isTableOrdered(tableId) && !diningService.isTableBilled(tableId)) {
                System.out.println("您输入的桌号还未预定,不能点餐,请重新输入.\n");
                continue;
            }
            break;//如果能到这一步说明输入的是正确的,则退出循环,执行下一步
        }

        //2,输入菜品编号
        int menuId = 0;
        while (true) {
            System.out.print("请输入菜品编号(-1退出):");
            menuId = Utility.readInt(5);
            if (menuId == -1) {//退出点餐
                return;
            }
            if (menuService .getMenu(menuId) == null) {//判断菜品编号是否存在
                System.out.println("您输入的菜品编号有误,请重新输入(-1退出).\n");
                continue;
            }
            break;//如果能到这一步说明输入的是正确的,则退出循环,执行下一步
        }

        //3,输入需要点的菜的份数
        int nums = 0;
        while (true) {
            System.out.print("每次最多10份,请输入需要点几份,(-1退出):");
            nums = Utility.readInt(2);
            if (nums == -1) {//退出点餐
                return;
            }
            if (nums > 10 || nums < 0) {
                System.out.println("超过最大数量,请重新输入(-1退出).\n");
                continue;
            }
            break;//如果能到这一步说明输入的是正确的,则退出循环,执行下一步
        }

        boolean b = billService.orderBill(tableId, menuId, nums);//返回是否点餐成功
        String menuName = menuService.getMenu(menuId).getName();
        if (b) {
            System.out.println(tableId + "号桌," + menuName + "," + nums + "份,点餐成功");
        } else {
            System.out.println(tableId + "号桌," + menuName +",点餐失败,请重新点餐");
        }
    }
    // 5 查看账单
    public void showBills() {
        System.out.println("\t\t 1 查看所有账单");
        System.out.println("\t\t 2 查看某一桌账单");
        System.out.print("请输入与您的选择:");
        String c = Utility.readString(1);
        switch (c) {
            case "1" -> {
                List<Bill> allBillsList = billService.getAllBill();
                System.out.println("账单id\t菜品编号\t份数\t金额\t\t餐桌\t\t订餐时间\t\t\t\t\t账单状态\t付款方式");
                for (Bill bill:allBillsList) {
                    System.out.println(bill);
                }
            }
            case "2" -> {
                System.out.print("请输入需要查询账单的桌号(-1退出):");
                int tableId = Utility.readInt();
                while (true) {
                    if (tableId == -1) {
                        return;
                    }
                    if (!diningService.isTableExist(tableId)) {
                        System.out.println("您输入的餐桌号有误,请重新输入(-1退出)");
                        continue;
                    }
                    break;
                }
                List<Bill> specifyBill = billService.getUncheckedBill(tableId);
                if (specifyBill.isEmpty()) {
                    System.out.println("该餐桌没有账单!");
                    return;
                }
                System.out.println("账单id\t菜品编号\t份数\t金额\t\t餐桌\t\t订餐时间\t\t\t\t\t账单状态\t付款方式");
                for (Bill bill : specifyBill) {
                    System.out.println(bill);
                }
            }
        }
    }
    // 6 结账
    public void payBill() {
        int tableId;
        while (true) {//循环获得输入结果,直到获得正确的输入或者-1退出
            System.out.print("请输入需要结账的桌号(-1退出):");
            tableId = Utility.readInt(5);
            if (tableId == -1) {
                return;
            }
            if (!diningService.isTableExist(tableId)) {
                System.out.println("您输入的餐桌不存在,请重新输入(-1退出)\n");
                continue;
            }
            if (diningService.isTableOrdered(tableId)) {//餐桌已预定但是未点餐
                System.out.println("此餐桌已预定但还未点餐,无法进行结账,请重新输入(-1退出)\n");
                continue;
            }
            if (diningService.isTableNull(tableId)) {//餐桌为空,还未预定
                System.out.println("此餐桌还未预定,无法进行结账,请重新输入(-1退出)\n");
                continue;
            }
            break;
        }

        //列出该桌所有账单
        List<Bill> specifyBill = billService.getUncheckedBill(tableId);
        System.out.println("账单id\t菜品编号\t份数\t金额\t\t餐桌\t\t订餐时间\t\t\t\t\t账单状态\t付款方式");
        for (Bill bill : specifyBill) {//显示该桌账单
            System.out.println(bill);
        }
        //显示该桌总金额
        System.out.println("总金额为:" + billService.getTotalMoney(tableId));
        //输入付款方式
        System.out.print("使用1 现金,2 支付宝还是3 微信呢?请输入数字选择(-1退出):");
        int p = Utility.readInt();
        if (p == -1) {
            return;
        }
        //进行结账
        String payType = (p == 1 ? "现金" : (p == 2 ? "支付宝" : "微信"));//三目运算符,根据输入的数字得到相应的内容
        boolean bool = billService.setBillState(tableId, "已结账", payType);//进行结账并返回执行结果
        System.out.println(bool ? tableId+"号桌结账成功,欢迎下次光临!\n" : tableId+"号桌结账失败,请重新结账!\n");
    }
}


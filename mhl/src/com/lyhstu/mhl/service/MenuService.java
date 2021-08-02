package com.lyhstu.mhl.service;

import com.lyhstu.mhl.dao.MenuDAO;
import com.lyhstu.mhl.domain.Menu;

import java.util.List;

public class MenuService {
    private final MenuDAO menuDAO = new MenuDAO();

    /**
     * 显示菜单
     * @return 返回Menu菜单类对象List集合
     */
    public List<Menu> getMenuList() {
        String sql = "select * from menu";
        return menuDAO.queryMulti(sql, Menu.class);
    }

    /**
     * 返回一个Menu菜品对象
     * @param menuId 菜品号
     * @return 返回菜品对象
     */
    public Menu getMenu(int menuId) {
        String sql = "select * from menu where id = ?";
        return menuDAO.querySingle(sql, Menu.class, menuId);
    }
}

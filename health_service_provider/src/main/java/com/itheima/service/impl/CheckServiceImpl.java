package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import com.itheima.dao.CheckItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther:liyang
 * @Date:2021/11/12 - 11 -12 -10:29
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        //调用dao层的方法
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //当前页面，但是如果当前页码不是首页，则会出现异常
        //在进行条件查询的时候，应该将第一页设置为首页
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> pages = checkItemDao.selectByCondition(queryString);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public void delete(Integer id) throws RuntimeException {
        //查询当前检查项是否和检查组关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            //当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem selectById(Integer id) {
        return checkItemDao.selectById(id);
    }

    @Override
    public void editCheckItem(CheckItem checkItem) {
        checkItemDao.editCheckItem(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

}

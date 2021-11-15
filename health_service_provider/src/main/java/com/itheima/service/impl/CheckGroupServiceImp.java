package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/13 - 11 -13 -19:31
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImp implements CheckGroupService {
    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //将组的信息加入到其中
        checkGroupDao.add(checkGroup);
        //将组合项的信息绑定起来
        setCheckGroupAndCheckitem(checkGroup.getId(), checkitemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> pages = checkGroupDao.queryByCondition(queryString);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public Integer[] findCheckitems(Integer id) {

        return checkGroupDao.findCheckitemIds(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //修改checkGroup的数据
        checkGroupDao.edit(checkGroup);
        //修改关联表的数据
        System.out.println(checkitemIds);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }


    public void setCheckGroupAndCheckitem(Integer checkGroupId, Integer[] CheckitemIds) {
        if (CheckitemIds != null && CheckitemIds.length >= 0) {
            for (Integer integer : CheckitemIds) {
                Map map = new HashMap<Integer, Integer>();
                map.put("checkGroupId", checkGroupId);
                map.put("checkId", integer);
                checkGroupDao.setCheckGroupIdAndCheckitem(map);
            }
        }
    }
}

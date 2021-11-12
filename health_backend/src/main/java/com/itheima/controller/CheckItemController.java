package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:liyang
 * @Date:2021/11/12 - 11 -12 -10:12
 * @Description:com.itheima.controller
 * @Version:1.0 检查项管理
 */

//相当于@responseBody 和@RestController的和
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    CheckItemService checkItemService;

    //新增检查项
    @RequestMapping("/add")
    public Result addCheckItem(@RequestBody CheckItem checkItem) {
        //将数据保存到数据库
        //数据保存成功了
        Result result = null;
        try {

            checkItemService.add(checkItem);
            result = new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        }
        //数据保存失败了
        catch (Exception exception) {
            //新增检查项失败了
            exception.printStackTrace();
            result = new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return result;
    }

    @RequestMapping("/pageQuery")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/delete")
    public Result deleteCheckItem(Integer id) {

        try {
            checkItemService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/selectById")
    public Result selectById(Integer id) {
        CheckItem checkItem = null;
        try {

            checkItem = checkItemService.selectById(id);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }

    @RequestMapping("/edit")
    public Result editCheckItem(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.editCheckItem(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);

    }
}


package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import javax.naming.Name;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/14 - 11 -14 -20:32
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImp implements SetMealService {
    @Autowired
    SetMealDao setMealDao;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputpath;

    //后台操作向服务器中添加套餐数据
    @Override
    public void add(Setmeal setmeal, Integer[] integers) {
        //向两长表中插入数据
        setMealDao.add(setmeal);
        setSetMealAndCheckGroup(setmeal, integers);
        savePic2Redis(setmeal.getImg());
        this.getStaticSetmelHtml();
    }

    public void getStaticSetmelHtml() {
        List<Setmeal> setmeals = setMealDao.findAll();
        for (Setmeal setmeal : setmeals) {
            setmeal.getImg();
        }
        this.getSetmealsHtml(setmeals);
        this.getDetailSteamlsHtml(setmeals);
    }

    //生成主套餐页面
    public void getSetmealsHtml(List<Setmeal> setmeals) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("setmealList", setmeals);
        this.getGeneralHtml("mobile_setmeal.ftl", map, "m_setmeal.html");
    }

    //生成所有套餐详情页面
    public void getDetailSteamlsHtml(List<Setmeal> setmeals) {
        //遍历list集合拿到每一个setmeal
        for (Setmeal setmeal : setmeals) {
            HashMap<String, Object> map = new HashMap<>();
            //查表
            Setmeal setmeal1 = setMealDao.findById(setmeal.getId());
            map.put("setmeal", setmeal1);
            this.getGeneralHtml("mobile_setmeal_detail.ftl", map, "setmeal_detail_" + setmeal.getId() + ".html");
        }
    }

    //   创建静态页面的方法
    public void getGeneralHtml(String templateName, Map<String, Object> map, String HtmlName) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            // 创建写出流
            File docFile = new File(outputpath + "\\" + HtmlName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            //创建静态文件
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将图片名称保存到Redis
    //只有这样添加的数据被加入到了redis中@
    //将图片名保存在redis中的方法
    private void savePic2Redis(String pic) {
        //添加到set集合中，把中数据库中查询出来的img
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    //    分页方法，在页面开始加载的时候加载
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> pages = setMealDao.queryByCondition(queryString);
        if (pages != null && pages.size() > 0) {
            for (Setmeal setmeal : pages.getResult()) {
                savePic2Redis(setmeal.getImg());
            }
        }

        return new PageResult(pages.getTotal(), pages.getResult());
    }

    //    返回所有的套餐信息
    @Override
    public List<Setmeal> getSetMeals() {
        return setMealDao.getSetMeals();
    }

    //查询单个的套餐信息
    @Override
    public Setmeal findSetMealById(int id) {
        return setMealDao.findById(id);
    }

    //    返回单个套餐的是详细信息，报告检查组，检查项

    private void setSetMealAndCheckGroup(Setmeal setmeal, Integer[] integers) {
        Map map = new HashMap<>();
        for (Integer id : integers) {
            map.put("setMealId", setmeal.getId());
            map.put("checkGroupId", id);
            setMealDao.setSetMealAndCheckGroup(map);
        }
    }


}

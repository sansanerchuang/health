package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Auther:liyang
 * @Date:2021/11/23 - 11 -23 -15:42
 * @Description:com.itheima.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("/verifyCode")
public class VerifyCodeController {
    @Autowired
    private JedisPool jedisPool;

    //send4Order
    @RequestMapping("/send4Order")
    public Result Code4Order(String telephone) {
//             生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
//        发送验证码
//        try {
//            //发送短信
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
//        } catch (ClientException e) {
//            e.printStackTrace();
//            //验证码发送失败
//            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
//        }
//        将验证码存入到redis中
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}

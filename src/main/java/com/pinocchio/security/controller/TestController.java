package com.pinocchio.security.controller;

import com.pinocchio.security.SecurityApplication;
import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.model.TestJsonDomain;
import com.pinocchio.security.mqttSender.IMqttSender;
import com.pinocchio.security.util.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private IMqttSender iMqttSender;

    @RequestMapping("/testJson")
    public R testJson(@RequestBody TestJsonDomain domain) {
        System.out.println(domain.toString());
        return R.ok();
    }

    @RequestMapping("/mqtt")
    public void test() {
        iMqttSender.sendToMqtt("creq/199","$cmd=setalertor&switch=on&msgid=14");
        System.out.println("success");
    }
}

package com.pinocchio.security;

import com.pinocchio.security.grpc.HellowWorldClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityApplication.class})
public class GrpcTest {
    @Autowired
    private HellowWorldClient helloWorldClient;

    @Test
    public void testSayHello() {
        String result = helloWorldClient.sayHello("Grpc", "Java");
        int a = helloWorldClient.addOperation(1, 2);
        System.out.println("sayHello的结果为：" + "    " + result);
        System.out.println("addOperation的结果为：" + "    " + a);
    }

}

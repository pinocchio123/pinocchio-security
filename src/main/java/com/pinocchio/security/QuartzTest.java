package com.pinocchio.security;

import com.pinocchio.security.quartzTask.QuartzJob;
import com.pinocchio.security.util.QuartzManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityApplication.class})// 指定启动类
public class QuartzTest {
    @Autowired
    private QuartzManager quartzManager;

    @Test
    public void test() {
//      quartzManager.addJob("test1","testGroup1","testTrigger1","testTriggerGroup1",QuartzJob.class,"0 */1 * * * ?");
        quartzManager.removeJob("test", "testGroup", "testTrigger", "testTriggerGroup");
    }
}

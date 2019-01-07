package com.pinocchio.security;

import com.pinocchio.security.model.SysUser;
import com.pinocchio.security.model.UserVo;
import com.pinocchio.security.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityApplicationTests {
	@Autowired
	private SysUserService userService;

	@Test
	public void contextLoads() {
		List<UserVo> users = userService.queryUser();
		System.out.println(users.toString());
	}

}


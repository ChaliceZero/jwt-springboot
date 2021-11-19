package com.zq.jwt.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zq.jwt.common.service.UserLoginService;
import com.zq.jwt.consts.HttpState;
import com.zq.jwt.model.SysUser;
import com.zq.jwt.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private UserLoginService userLoginService;

    /**
     * 用户登录认证
     *
     * @param model
     * @return
     */
    @PostMapping("/login")
    public Map<String, Object> login(SysUser model) {
        Map<String, Object> res = new HashMap<>();
        try {
            // 1、查询用户信息
            List<SysUser> users = sysUserService.select(model);
            if (ObjectUtil.isEmpty(users)) {
                throw new RuntimeException("用户查询为空");
            }
            SysUser sysUser = users.get(0);
            // 2、认证用户、生成登录token
            // 用户标识，可使用用户ID作为标识，也可每次登录时随机生成
            String userKey = IdUtil.simpleUUID();
            String token = userLoginService.login(sysUser.getUsername(), sysUser.getPassword(), userKey);
            // 3、操作其他业务，如：插入/更新用户登录信息 TODO

            System.out.println("当前登录用户标识=" + userKey);
            res.put("state", HttpState.SUCCESS);
            res.put("msg", "认证成功");
            res.put("data", token);
        } catch (Exception e) {
            res.put("state", HttpState.ERROR);
            res.put("msg", "认证失败");
        }
        return res;
    }

    /**
     * 用户登录认证
     *
     * @return
     */
    @GetMapping("getLoginInfo")
    public Map<String, Object> getLoginInfo() {
        Map<String, Object> res = new HashMap<>();
        try {
            String loginInfo = userLoginService.getLoginInfo();
            System.out.println("获取信息用户标识=" + loginInfo);
            res.put("state", HttpState.SUCCESS);
            res.put("msg", "操作成功");
            res.put("data", loginInfo);
        } catch (Exception e) {
            res.put("state", HttpState.ERROR);
            res.put("msg", "操作失败");
        }
        return res;
    }
}
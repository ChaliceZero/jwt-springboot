package com.zq.jwt.service;

import com.zq.jwt.model.SysUser;

import java.util.List;

/**
 * @author laozhou
 */
public interface SysUserService {

    int insert(SysUser sysUser);

    int delete(SysUser sysUser);

    int update(SysUser sysUser);

    List<SysUser> select(SysUser sysUser);
}
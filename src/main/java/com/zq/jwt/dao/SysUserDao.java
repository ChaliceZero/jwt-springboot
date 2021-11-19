package com.zq.jwt.dao;

import com.zq.jwt.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author
 * @description 使用注解【@Repository】注入失败
 */
@Mapper
public interface SysUserDao {

    int insert(SysUser sysUser);

    int delete(SysUser sysUser);

    int update(SysUser sysUser);

    List<SysUser> select(SysUser sysUser);
}
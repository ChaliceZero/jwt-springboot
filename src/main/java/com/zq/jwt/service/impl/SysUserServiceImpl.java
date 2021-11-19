package com.zq.jwt.service.impl;

import com.zq.jwt.service.SysUserService;
import com.zq.jwt.dao.SysUserDao;
import com.zq.jwt.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author laozhou
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysUser sysUser) {
        return sysUserDao.insert(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(SysUser sysUser) {
        return sysUserDao.delete(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysUser sysUser) {
        return sysUserDao.update(sysUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SysUser> select(SysUser sysUser) {
        return sysUserDao.select(sysUser);
    }

}
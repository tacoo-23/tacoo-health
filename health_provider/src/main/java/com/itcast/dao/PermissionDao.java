package com.itcast.dao;

import com.github.pagehelper.Page;
import com.itcast.pojo.Permission;

public interface PermissionDao {
    Page<Permission> findPage(String queryString);
}

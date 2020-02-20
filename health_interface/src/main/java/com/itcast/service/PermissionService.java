package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;

public interface PermissionService {
    PageResult findPage(QueryPageBean pageBean);
}

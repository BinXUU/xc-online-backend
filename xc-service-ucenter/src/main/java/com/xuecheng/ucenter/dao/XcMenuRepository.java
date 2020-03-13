package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by BinXU on 2020/02/22.
 */
public interface XcMenuRepository extends JpaRepository<XcMenu, String> {
}

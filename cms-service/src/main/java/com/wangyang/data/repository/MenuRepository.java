package com.wangyang.data.repository;

import com.wangyang.model.pojo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu,Integer> {

    Menu findByCategoryId(int id);
    Menu findBySheetId(int id);
}

package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Channel;
import com.wangyang.cms.pojo.vo.ChannelVo;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface IChannelService extends IBaseCategoryService<Channel>{
    Channel addOrUpdate(Channel channel);


    Channel save(Channel channel);

    Channel deleteById(int id);

    Channel findById(int channelId);

    ChannelVo convent(Channel channel);

    Channel addOrRemoveToMenu(int id);

    ModelAndView preview(Integer id);

    Channel updateFirstSheet(int id, String name);

    List<Channel> list();
}

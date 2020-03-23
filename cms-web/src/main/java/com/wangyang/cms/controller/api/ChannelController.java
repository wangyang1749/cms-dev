package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Channel;
import com.wangyang.cms.pojo.params.ChannelParam;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.service.IChannelService;
import com.wangyang.cms.service.IHtmlService;
import com.wangyang.cms.utils.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/channel")
public class ChannelController {

    @Autowired
    IChannelService channelService;

    @Autowired
    IHtmlService htmlService;

    @PostMapping
    public Channel add(@RequestBody ChannelParam channelParam){
        Channel channel = new Channel();
        BeanUtils.copyProperties(channelParam,channel);
        Channel addChannel = channelService.addOrUpdate(channel);
        htmlService.generateChannelListHtml();
        return addChannel;
    }

    @PostMapping("/update/{id}")
    public Channel update(@PathVariable("id") Integer id,@RequestBody ChannelParam channelParam){
        Channel channel = channelService.findById(id);
        BeanUtils.copyProperties(channelParam,channel);
        Channel updateChannel = channelService.addOrUpdate(channel);
        htmlService.generateChannelListHtml();
        return updateChannel;
    }

    @GetMapping("/delete/{id}")
    public Channel deleteById(@PathVariable("id") Integer id){
        Channel channel = channelService.deleteById(id);
        //TODO 没有删除该栏目下从的所有文章,只是单纯删除栏目
        htmlService.generateChannelListHtml();
        return channel;
    }


    @GetMapping("/find/{id}")
    public Channel findById(@PathVariable("id") Integer id){
        return  channelService.findById(id);
    }
    @GetMapping
    public List<Channel> listAll(){
        return channelService.list();
    }

    @GetMapping("/addOrRemoveToMenu/{id}")
    public Channel addOrRemoveToMenu(@PathVariable("id") int id){
        Channel channel = channelService.addOrRemoveToMenu(id);
        htmlService.generateMenuListHtml();
        return channel;
    }

    @GetMapping("/updateFirstSheet/{id}")
    public Channel updateFirstSheet(@PathVariable("id") int id,String name){
        Channel channel = channelService.updateFirstSheet(id, name);
        htmlService.generateChannelListHtml();
        if(channel.getExistNav()){
            htmlService.generateMenuListHtml();
        }
        return channel;
    }

    @GetMapping("/updateAll")
    public Set<String> updateAllHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Channel> channels = channelService.list();
        channels.forEach(channel -> {
            if(more){
                channel.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_CHANNEL_TEMPLATE);
                channel.setTemplateName(CmsConst.DEFAULT_CHANNEL_TEMPLATE);
                channelService.save(channel);
            }
            htmlService.generateSheetListByChannelId(channel.getId());
        });
        return  ServiceUtil.fetchProperty(channels, Channel::getName);
    }

    @GetMapping("/generateHtml/{id}")
    public Channel generateHtml(@PathVariable("id") Integer id){
        Channel channel = channelService.findById(id);
        htmlService.conventHtml(channel);
        return channel;
    }
}

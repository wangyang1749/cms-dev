package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.dto.SheetDto;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.vo.ChannelVo;
import com.wangyang.cms.repository.ChannelRepository;
import com.wangyang.cms.repository.MenuRepository;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.IChannelService;
import com.wangyang.cms.service.ISheetService;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.utils.CMSUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Service
@TemplateOption
public class ChannelServiceImpl extends BaseCategoryServiceImpl<Channel> implements IChannelService {

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    IArticleService articleService;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ITemplateService templateService;

    @Override
    public Channel addOrUpdate(Channel channel){
        if(channel.getHaveHtml()==null){
            channel.setHaveHtml(true);
        }
        if (StringUtils.isEmpty(channel.getViewName())) {
            String viewName = CMSUtils.randomViewName();
            channel.setViewName(viewName);
        }
        if(channel.getTemplateName()==null|| "".equals(channel.getTemplateName())){
            channel.setTemplateName(CmsConst.DEFAULT_CHANNEL_TEMPLATE);
        }
        if(channel.getArticleTemplateName()==null|| "".equals(channel.getArticleTemplateName())){
            channel.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_CHANNEL_TEMPLATE);
        }
        return  channelRepository.save(channel);
    }

    @Override
    public Channel save(Channel channel){
        return  channelRepository.save(channel);
    }


    @Override
    public Channel deleteById(int id){
        Channel channel = findById(id);
        channelRepository.deleteById(channel.getId());
        return channel;
    }

    @Override
    public Channel findById(int channelId){
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);
        if(optionalChannel.isPresent()){
            return optionalChannel.get();
        }
        throw  new ObjectException("Channel 没有找到!!");
    }


    @Override
    public ChannelVo convent(Channel channel){
        ChannelVo channelVo = new ChannelVo();
        BeanUtils.copyProperties(channel,channelVo);
        List<ArticleDto> articleDtos = articleService.listBy(channel.getId());
        channelVo.setArticles(articleDtos);
        return channelVo;
    }

    @Override
    public Channel addOrRemoveToMenu(int id){
        Channel channel = findById(id);
        Menu menu = menuRepository.findByCategoryId(channel.getId());
        if(channel.getExistNav()){
            channel.setExistNav(false);
            if(menu!=null){
                menuRepository.deleteById(menu.getId());
            }
        }else{
            channel.setExistNav(true);
            if(menu==null){
                menu = new Menu();
            }

            menu.setName(channel.getName());
            menu.setCategoryId(channel.getId());
            menu.setUrlName(channel.getPath()+"/"+channel.getName()+"/"+channel.getFirstArticle()+".html");
            menuRepository.save(menu);

        }
        return  channelRepository.save(channel);
    }

    @Override
    public ModelAndView preview(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Channel channel = findById(id);
        ChannelVo channelVo = convent(channel);

        Template template = templateService.findByEnName(channelVo.getTemplateName());
        modelAndView.addObject("view", channelVo);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }

    @Override
    public Channel updateFirstSheet(int id, String name){
        Channel channel = findById(id);
        channel.setFirstArticle(name);
        return  channelRepository.save(channel);
    }

    @Override
    @TemplateOptionMethod(name = "Channel List", templateValue = "templates/components/@channelList", viewName = "channelList", path = "components")
    public List<Channel> list(){
        return channelRepository.findAll();
    }
}

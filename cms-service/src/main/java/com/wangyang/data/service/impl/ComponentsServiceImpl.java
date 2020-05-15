package com.wangyang.data.service.impl;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.exception.TemplateException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.ApplicationBean;
import com.wangyang.data.repository.ArticleRepository;
import com.wangyang.data.repository.ComponentsArticleRepository;
import com.wangyang.data.service.IArticleService;
import com.wangyang.data.service.IComponentsService;
import com.wangyang.data.service.ITagsService;
import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.entity.*;
import com.wangyang.data.repository.ComponentsRepository;
import com.wangyang.model.pojo.params.ArticleQuery;
import com.wangyang.model.pojo.params.ComponentsParam;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComponentsServiceImpl implements IComponentsService {

    @Autowired
    ComponentsRepository componentsRepository;

    @Autowired
    IArticleService articleService;

    @Autowired
    ITagsService tagsService;


    @Override
    public Page<Components> list(Pageable pageable){
        return componentsRepository.findAll(pageable);
    }

    @Override
    public List<Components> listNeedArticle(){
        Specification<Components> specification = new Specification<Components>() {
            @Override
            public Predicate toPredicate(Root<Components> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("dataName"),"@Article")).getRestriction();
            }
        };
        return componentsRepository.findAll(specification);
    }


    @Override
    public Components add(ComponentsParam componentsParam){
        Components components = new Components();
        convert(components,componentsParam);
        components.setIsSystem(false);
        return componentsRepository.save(components);
    }



    @Override
    public Components update(int id, ComponentsParam componentsParam){
        Components components = findById(id);
        convert(components,componentsParam);
        return componentsRepository.save(components);
    }

    private void convert(Components components,ComponentsParam componentsParam){
        BeanUtils.copyProperties(componentsParam,components,"templateValue");
        String templateValue =componentsParam.getTemplateValue();
        if(templateValue.startsWith("templates")){
            String templateValueName = templateValue.split("\n")[0];
            String path = CmsConst.WORK_DIR+"/"+templateValueName+".html";
            File file = new File(path);
            String fileTemplateValue = componentsParam.getTemplateValue();
            components.setTemplateValue(templateValueName);
            String replaceFileTemplateValue = fileTemplateValue.replace(templateValueName+"\n", "");
            FileUtils.saveFile(file,replaceFileTemplateValue);
        }else {
            components.setTemplateValue(componentsParam.getTemplateValue());
        }
    }

    @Override
    public List<Components> addAll(List<Components> templatePages) {
        return componentsRepository.saveAll(templatePages);
    }

    @Override
    public Components findById(int id){
        Optional<Components> templatePageOptional = componentsRepository.findById(id);
        if(!templatePageOptional.isPresent()){
            throw new TemplateException("add template did't exist!!");
        }
        return templatePageOptional.get();
    }


    @Override
    public Components findDetailsById(int id){
        Components components = findById(id);
        String templateValue = components.getTemplateValue();
        if(templateValue.startsWith("templates")){
            String path = CmsConst.WORK_DIR+"/"+templateValue+".html";
            File file = new File(path);
            if(file.exists()){
                String openFile = FileUtils.openFile(file);
                components.setTemplateValue(components.getTemplateValue()+"\n"+openFile);
            }
        }
        return components;
    }

    @Override
    public Components delete(int id){
        Components components = findById(id);
        if(components.getIsSystem()){
            throw new ObjectException("系统内置模板不能删除");
        }
        componentsRepository.deleteById(id);
        return components;
    }

    @Override
    public void deleteAll() {
        componentsRepository.deleteAll();
    }






    @Override
    public Object getModel(Components components) {
        try {
            if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA)){

                return  articleService.listByComponentsId(components.getId());


//                Set<Integer> ids = Arrays.asList(args).stream().map(a -> Integer.parseInt(a)).collect(Collectors.toSet());
//                String[] names = components.getDataName().substring(1).split("\\.");
//                String className = names[0];
//                String methodName = names[1];
//                Object bean = ApplicationBean.getBean(className);
//                Method method = bean.getClass().getMethod(methodName,Set.class);
//                Object o = method.invoke(bean,ids);
//                return o;
            }else if (components.getDataName().startsWith("articleJob")){
                String[] names = components.getDataName().split("\\.");
                String className = names[0];
                String methodName = names[1];
                Object bean = ApplicationBean.getBean(className);
                Method method = bean.getClass().getMethod(methodName);
                Object o = method.invoke(bean);
                return o;

            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_SORT)){
                Specification<Article> specification = new Specification<Article>() {
                    @Override
                    public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        return null;
                    }
                };
                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_SORT.length());
                Sort sort;
                if(args!=null||!"".equals(args)){
                    String[] argsArray = args.split(",");
                    String directionStr = argsArray[argsArray.length-1];
                    if(directionStr.equals("DESC")||directionStr.equals("ASC")){
                        Sort.Direction direction = Sort.Direction.valueOf(directionStr);
                        sort = Sort.by(direction, Arrays.copyOf(argsArray,argsArray.length-1));
                    }else {
                        sort = Sort.by( argsArray);
                    }
                }else {
                    sort = Sort.by(Sort.Order.desc("id"));
                }

                Page<ArticleDto> articleDtos = articleService.articleShow(specification, PageRequest.of(0, 5,sort ));

                Map<String,Object> map = new HashMap<>();
                map.put("view",articleDtos);
                map.put("showUrl","/articleList?sort="+args); //likes,DESC
                map.put("name",components.getName());
                return map;
//                Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
//                TemplateUtil.convertHtmlAndSave();
            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_KEYWORD)){

                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_KEYWORD.length());
                ArticleQuery articleQuery = new ArticleQuery();
                articleQuery.setKeyword(args);
                Page<ArticleDto> pageDto = articleService.pageDtoBy(PageRequest.of(0, 5, Sort.by(Sort.Order.desc("updateDate"))), articleQuery);
                Map<String,Object> map = new HashMap<>();
                map.put("view",pageDto);
                map.put("showUrl","/articleList?keyword="+args); //
                map.put("name",components.getName());
                return map;
            }else if(components.getDataName().startsWith(CmsConst.ARTICLE_DATA_TAGS)) {
                String args = components.getDataName().substring(CmsConst.ARTICLE_DATA_TAGS.length());
                Optional<Tags> tags = tagsService.findBy(args);
                if(tags.isPresent()){
                    Page<ArticleDto> articleDtos = articleService.pageByTagId(tags.get().getId(), 5);
                    Map<String,Object> map = new HashMap<>();
                    map.put("view",articleDtos);
                    map.put("showUrl","/articleList?tagsId="+tags.get().getId());
                    map.put("name",components.getName());
                    return map;
                }
            }else {

            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Components findByDataName(String dataName){
        List<Components> templatePages = componentsRepository.findAll(new Specification<Components>() {
            @Override
            public Predicate toPredicate(Root<Components> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("dataName"), dataName)
                        ,criteriaBuilder.isTrue(root.get("status"))).getRestriction();
            }
        });

        if(CollectionUtils.isEmpty(templatePages)){
            throw new TemplateException("Template Not found!!");
        }

        return templatePages.get(0);
    }

    @Override
    public Components findByViewName(String viewName){
        return componentsRepository.findByViewName(viewName);
    }

}

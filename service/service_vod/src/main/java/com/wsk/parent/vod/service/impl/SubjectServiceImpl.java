package com.wsk.parent.vod.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.vod.Subject;
import com.wsk.ggkt.vo.vod.SubjectEeVo;
import com.wsk.parent.vod.listner.SubjectListener;
import com.wsk.parent.vod.mapper.SubjectMapper;
import com.wsk.parent.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Autowired
    SubjectMapper subjectMapper;
    @Autowired
    SubjectListener listener;

    @Override
    //查询下一级课程分类
    public List<Subject> getChildSubject(Long id) {
        LambdaQueryWrapper<Subject> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Subject::getParentId,id);
        List<Subject> subjectList = subjectMapper.selectList(lambdaQueryWrapper);
        //判断ID下面是否还有子节点
        //TODO 这里应该可以优化，不循环访问数据库
        for(Subject subject : subjectList){
            Long subjectId = subject.getId();
            boolean result = this.hasChild(subjectId);
            subject.setHasChildren(result);
        }
        return subjectList;
    }

    @Override
    //课程分类导出
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            List<Subject> dictList = subjectMapper.selectList(null);//查询所有的课程分类
            List<SubjectEeVo> dictVoList = new ArrayList<>(dictList.size());
            //将subject 转换成 subjectEeVo
            for(Subject subject:dictList){
                SubjectEeVo vo = new SubjectEeVo();
                BeanUtils.copyProperties(subject,vo);//复制属性
                dictVoList.add(vo);
            }
            //easyExcel写操作
            EasyExcel.write(response.getOutputStream(),SubjectEeVo.class).sheet("课程分类").doWrite(dictVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //导入课程分类
    @Override
    public void importDate(MultipartFile file) {
        try {
            //调用easyExcel的方法
            EasyExcel.read(file.getInputStream(),SubjectEeVo.class,listener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断是否有子节点
    public boolean hasChild(Long id){
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getParentId,id);
        Integer count = subjectMapper.selectCount(queryWrapper);
        return count>0;
    }
}

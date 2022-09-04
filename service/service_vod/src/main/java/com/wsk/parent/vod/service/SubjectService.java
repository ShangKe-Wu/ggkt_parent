package com.wsk.parent.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.vod.Subject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
public interface SubjectService extends IService<Subject> {

    //查询下一级课程分类，懒加载
    List<Subject> getChildSubject(Long id);

    //课程分类导出
    void exportData(HttpServletResponse response);

    //课程分类导入
    void importDate(MultipartFile file);
}

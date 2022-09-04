package com.wsk.parent.vod.service.impl;

import com.wsk.ggkt.model.vod.VideoVisitor;
import com.wsk.ggkt.vo.vod.VideoVisitorCountVo;
import com.wsk.parent.vod.mapper.VideoVisitorMapper;
import com.wsk.parent.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    //统计数据：某个课程在某个时间段内的观看数据
    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        //调用mapper的方法
        List<VideoVisitorCountVo> videoVisitorVoList =
                baseMapper.findCount(courseId,startDate,endDate);
        //创建map集合
        Map<String, Object> map = new HashMap<>();
        //创建两个list集合，一个代表所有日期，一个代表日期对应数量
        //封装数据  代表所有日期
        List<String> dateList =new ArrayList<>();
        for (VideoVisitorCountVo videoVisitorCountVo : videoVisitorVoList) {
            String joinTime = videoVisitorCountVo.getJoinTime();
            dateList.add(joinTime);
        }
        //代表日期对应数量
        List<Integer> countList = videoVisitorVoList.stream().map(VideoVisitorCountVo::getUserCount)
                .collect(Collectors.toList());
        //放到map集合
        map.put("xData", dateList);//横坐标
        map.put("yData", countList);//纵坐标
        return map;
    }
}

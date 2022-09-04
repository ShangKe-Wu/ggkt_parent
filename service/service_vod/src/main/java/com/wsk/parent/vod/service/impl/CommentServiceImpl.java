package com.wsk.parent.vod.service.impl;

import com.wsk.ggkt.model.vod.Comment;
import com.wsk.parent.vod.mapper.CommentMapper;
import com.wsk.parent.vod.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}

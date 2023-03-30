package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.entity.Comment;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.mapper.CommentMapper;
import com.wanghui.blog.mapper.UserMapper;
import com.wanghui.blog.service.CommentService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.CommentVo;
import com.wanghui.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlunit.util.Mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-24 10:12:31
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult selectAllByArtticleId(String pageNum, String pageSize, String articleId) {
        //编辑查询sql
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Comment::getArticleId,articleId);
        //创建一个分页对象
        Page page = new Page(Long.valueOf(pageNum), Long.valueOf(pageSize));
        //根据文章id获取分页后的评论信息,并封装到page对象中
        Page<Comment> commentPage = commentMapper.selectPage(page, lambdaQueryWrapper);
        //获取page对象中封装的评论信息
        List<Comment> commentList = commentPage.getRecords();
        //将该对象拷贝到commentVo对象中，commentVo才是应该向页面返回的评论对象，该对象中包含了评论对象的子对象
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        //获取commentVoList中每个评论对象的对象id，查询该对象的子评论，并封装到commentVoList对象的children属性中
        commentVoList= commentVoList.stream().map(commentVo -> {
            //获取评论对象子评论的集合
            LambdaQueryWrapper<Comment> lambdaQueryWrapper1 = new LambdaQueryWrapper();
            lambdaQueryWrapper1.eq(Comment::getRootId, commentVo.getId());
            List<Comment> commentChildrenList = commentMapper.selectList(lambdaQueryWrapper1);
            //将Comment对象列表转换成CommentVo对象列表
            List<CommentVo> commenVotChildrenList = BeanCopyUtils.copyBeanList(commentChildrenList, CommentVo.class);
            commenVotChildrenList = commenVotChildrenList.stream().map(commenVotChildren ->{
                //用评论信息的创建人id获取评论人姓名
                LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper();
                lambdaQueryWrapper2.eq(User::getId, commenVotChildren.getCreateBy());
                User user = userMapper.selectOne(lambdaQueryWrapper2);
                //给commentVo对象的username属性塞值
                commenVotChildren.setUsername(user.getUserName());
                //返回评论对象的子对象
                return commenVotChildren;
            }).collect(Collectors.toList());
            //给commentVo对象的children属性塞值
            commentVo.setChildren(commenVotChildrenList);
            //用评论信息的创建人id获取评论人姓名
            LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper();
            lambdaQueryWrapper2.eq(User::getId, commentVo.getCreateBy());
            User user = userMapper.selectOne(lambdaQueryWrapper2);
            //给commentVo对象的username属性塞值
            commentVo.setUsername(user.getUserName());
            return commentVo;
        }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(commentVoList,commentPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult saveComment(Comment comment) {
        int count = commentMapper.insert(comment);
        if(count!=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult commentList(String linkComment, Object o, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(linkComment.equals(CodeLibraryUtil.LINK_COMMENT),Comment::getType,CodeLibraryUtil.LINK_COMMENT);
        //创建一个page对象，设置分页信息
        Page page = new Page(Long.valueOf(pageNum), Long.valueOf(pageSize));
        //查询出分页数据
        Page<Comment> commentPage = commentMapper.selectPage(page, lambdaQueryWrapper);
        //获取page对象中封装的评论信息
        List<Comment> commentList = commentPage.getRecords();
        //将该对象拷贝到commentVo对象中，commentVo才是应该向页面返回的评论对象，该对象中包含了评论对象的子对象
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        //获取commentVoList中每个评论对象的对象id，查询该对象的子评论，并封装到commentVoList对象的children属性中
        commentVoList= commentVoList.stream().map(commentVo -> {
            //获取评论对象子评论的集合
            LambdaQueryWrapper<Comment> lambdaQueryWrapper1 = new LambdaQueryWrapper();
            lambdaQueryWrapper1.eq(Comment::getRootId, commentVo.getId());
            List<Comment> commentChildrenList = commentMapper.selectList(lambdaQueryWrapper1);
            //将Comment对象列表转换成CommentVo对象列表
            List<CommentVo> commenVotChildrenList = BeanCopyUtils.copyBeanList(commentChildrenList, CommentVo.class);
            commenVotChildrenList = commenVotChildrenList.stream().map(commenVotChildren ->{
                //用评论信息的创建人id获取评论人姓名
                LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper();
                lambdaQueryWrapper2.eq(User::getId, commenVotChildren.getCreateBy());
                User user = userMapper.selectOne(lambdaQueryWrapper2);
                //给commentVo对象的username属性塞值
                commenVotChildren.setUsername(user.getUserName());
                //返回评论对象的子对象
                return commenVotChildren;
            }).collect(Collectors.toList());
            //给commentVo对象的children属性塞值
            commentVo.setChildren(commenVotChildrenList);
            //用评论信息的创建人id获取评论人姓名
            LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper();
            lambdaQueryWrapper2.eq(User::getId, commentVo.getCreateBy());
            User user = userMapper.selectOne(lambdaQueryWrapper2);
            //给commentVo对象的username属性塞值
            commentVo.setUsername(user.getUserName());
            return commentVo;
        }).collect(Collectors.toList());
        //将分页数据封装到pageVo对象
        PageVo pageVo = new PageVo(commentVoList,commentPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

}

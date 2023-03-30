package com.wanghui.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.entity.Link;
import com.wanghui.blog.mapper.LinkMapper;
import com.wanghui.blog.service.LinkService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.LinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-24 16:47:44
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Autowired
    private  LinkMapper linkMapper;
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getStatus, CodeLibraryUtil.LINK_STATUS_NORMAL);
        List<Link> links = linkMapper.selectList(wrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }
}

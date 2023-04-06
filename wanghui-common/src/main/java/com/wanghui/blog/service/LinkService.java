package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.entity.Link;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 友链(Link)表服务接口
 *
 * @author wanghui
 * @since 2023-03-24 16:47:44
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize,String linkName,String status);

    ResponseResult selectLinkById(Long linkId);
}

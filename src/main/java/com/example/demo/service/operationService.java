package com.example.demo.service;

import com.example.demo.dto.articleDto;
import com.example.demo.util.Result;

import java.util.List;

public interface operationService {
    /**
     * 点赞
     * @param id 文章ID
     * @return
     */
    public Result addLike(Integer id);

    /**
     * 评论
     * @param id 评论的文章ID
     * @param text 评论的内容
     * @return
     */
    public Result comments(Integer id, String text);

    /**
     * 收藏
     * @param id 收藏的文章ID
     * @return
     */
    public Result collections(Integer id);

    /**
     * 查询收藏列表
     * @return
     */
    public Result articleOperation();

    /**
     * 查询历史记录
     * @return
     */
    public Result selectHistory();

    /**
     * 将评论映射成Dto
     * @param id
     * @return
     */
    public List<articleDto.commentDto> commentToDot(Integer id);
}

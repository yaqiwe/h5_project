package com.example.demo.service;

import com.example.demo.util.Result;

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
}

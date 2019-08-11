package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.ReplyDAO;
import com.liewmanchoi.zheye.model.Reply;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Service
public class ReplyService {
  @Autowired ReplyDAO replyDAO;

  public List<Reply> getRepliesByEntity(int entityId, int entityType) {
    return replyDAO.selectByEntity(entityId, entityType);
  }

  public int addReply(Reply reply) {
    return replyDAO.addReply(reply);
  }

  public int getRepliesCount(int entityId, int entityType) {
    return replyDAO.getRepliesCount(entityId, entityType);
  }

  public void deleteReply(int entityId, int entityType) {
    replyDAO.updateStatus(entityId, entityType, 1);
  }

  public Reply getReplyById(int id) {
    return replyDAO.getReplyById(id);
  }
}

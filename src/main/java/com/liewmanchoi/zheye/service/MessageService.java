package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.MessageDAO;
import com.liewmanchoi.zheye.model.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Service
public class MessageService {
  @Autowired MessageDAO messageDAO;

  public int addMessage(Message message) {
    return messageDAO.addMessage(message);
  }

  public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
    return messageDAO.getConversationDetail(conversationId, offset, limit);
  }

  public List<Message> getConversationList(int userId, int offset, int limit) {
    return messageDAO.getConversationList(userId, offset, limit);
  }

  public int getConversationUnreadCount(int userId, String conversationId) {
    return messageDAO.getConversationUnreadCount(userId, conversationId);
  }
}

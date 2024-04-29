package code.shubham.fiq.services;

import code.shubham.fiq.models.Message;

public interface Queue {

    String getName();

    void offer(Message message) throws Exception;

    Message poll(String pollerId, int offset);
}

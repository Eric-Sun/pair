package com.h13.pair.daos;

import com.h13.pair.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 用于持久化保存相关的关联数据，用于cache挂掉之后的恢复
 * 当连接断掉之后也会软删除这个数据
 * User: sunbo
 * Date: 13-11-26
 * Time: 下午4:06
 */
@Repository
public class SessionDAO {

    @Autowired
    JdbcTemplate j;


    public void create(String sessionId) {
        String sql = "insert into session(session_id,to_session_id,createtime,updatetime,status)" +
                " values (?,?,now(),now(),?)";
        j.update(sql, new Object[]{sessionId, Constants.DEFAULT_TO_SESSION_ID, Constants.SESSION.WAIT});
    }


    public void doPair(String sessionId, String toSessionId) {
        String sql = "update session set to_session_id=?, pairtime=now(), updatetime=now() where session_id=?";
        j.update(sql, new Object[]{toSessionId, sessionId});
    }


    public void close(String sessionId) {
        String sql = "update session set status=? where session_id=?";
        j.update(sql, new Object[]{Constants.SESSION.CLOSED, sessionId});
    }

}

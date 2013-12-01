package com.h13.pair.daos;

import com.h13.pair.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-26
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
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

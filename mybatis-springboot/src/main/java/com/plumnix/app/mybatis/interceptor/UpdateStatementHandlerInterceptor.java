package com.plumnix.app.mybatis.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

@Slf4j
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "update",
        args = {Statement.class})})
public class UpdateStatementHandlerInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler routingStatementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = routingStatementHandler.getBoundSql();
        log.info(new ObjectMapper().writeValueAsString(boundSql));

//        MySqlStatementParser parser = new MySqlStatementParser(boundSql.getSql());
//        SQLStatement statement = parser.parseStatement();
//        MySqlInsertStatement insert = (MySqlInsertStatement)statement;
//        insert.getTableName().getSimpleName();

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        log.warn(properties.toString());
    }
}

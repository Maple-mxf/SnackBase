package io.snackbase.metaroute.sql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

/**
 * {@link SnackBaseSQLTranslator} translator sql language;default use MySQL,
 * Not support other SQL
 * <p>
 * <p>
 * base on alibaba druid
 * all sql action(function,condition,expr,order by is a sql Object by druid)
 * <p>
 * <p>
 * {@link com.alibaba.druid.sql.ast.SQLObject}
 * {@link com.alibaba.druid.sql.repository.SchemaResolveVisitorFactory}
 *
 * @author maxuefeng
 * @since 2019/12/5
 */
final
class SnackBaseSQLTranslator {

    public void translator(String sql){

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        parser.parseAnalyze();
    }
}

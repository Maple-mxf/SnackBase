package io.snackbase.metaroute.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

import java.util.List;

/**
 * {@link com.alibaba.druid.sql.ast.SQLObject}
 *
 * @author maxuefeng
 * @since 2019/12/5
 */
public class TestDruidSQLParser {

    @Test
    public void baseApi() {

        // String sql = "select * from users u left join wallet w on(u.id=w.uid) right join wallet_log wl on(wl.wid = w.id) where u.id>9 group by u.area";
        String sql = "SELECT sb1,sb2,sb3 FROM (SELECT s1 AS sb1, s2 AS sb2, s3*2 AS sb3 FROM t1) AS sb WHERE sb1 > 1";

        MySqlStatementParser sqlStatementParser = new MySqlStatementParser(sql);

        // SQLStatement contain any data operation
        SQLStatement sqlStatement = sqlStatementParser.parseStatement();
        System.err.println(sqlStatement);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        // mysql
        System.err.println(visitor.getDbType());
        // []
        System.err.println(visitor.getFunctions());
        // [users.area]
        System.err.println(visitor.getGroupByColumns());
        // [users.*, users.id]
        System.err.println(visitor.getColumns());
        // []
        System.err.println(visitor.getOrderByColumns());
        // [users.id =, wallet.uid =, UNKNOWN.id > 9]  not alias
        // [users.id, wallet.uid, users.*, wallet.*, users.area] has alias
        System.err.println(visitor.getConditions());
        // []
        System.err.println(visitor.getParameters());
        // [users.id = wallet.uid]
        System.err.println(visitor.getRelationships());
        // 0
        System.err.println(visitor.getFeatures());
        // []
        System.err.println(visitor.getRepository().getObjects());
        // {users=Select, wallet=Select}
        System.err.println(visitor.getTables());
        // []
        System.err.println(visitor.getAggregateFunctions());
    }

    /**
     * test not pass
     * error message [error sp_statement. pos 6, line 1, column 1, token SELECT]
     * @see MySqlStatementParser
     */
    @Test
    public void testMySqlStatementParser(){

        String sql = "SELECT sb1,sb2,sb3 FROM (SELECT s1 AS sb1, s2 AS sb2, s3*2 AS sb3 FROM t1) AS sb WHERE sb1 > 1";
        MySqlStatementParser sqlStatementParser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = sqlStatementParser.parseSpStatement();
        System.err.println(sqlStatement.getDbType());
        System.err.println(sqlStatement.getHeadHintsDirect());
        System.err.println(sqlStatement.isAfterSemi());
        System.err.println(sqlStatement.getAttributes());
        System.err.println(sqlStatement.toLowerCaseString());
        System.err.println(sqlStatement.getChildren());
    }

    /**
     * test not pass
     * error message [syntax error, error in :'SELECT sb1,sb2,sb3 FROM (SELEC', expect ANALYZE, actual SELECT pos 6, line 1, column 1, token SELECT]
     */
    @Test
    public void testMySqlAnalyzeStatement(){
        String sql = "SELECT sb1,sb2,sb3 FROM (SELECT s1 AS sb1, s2 AS sb2, s3*2 AS sb3 FROM t1) AS sb WHERE sb1 > 1";
        MySqlStatementParser sqlStatementParser = new MySqlStatementParser(sql);

        //
        MySqlAnalyzeStatement parseAnalyze = sqlStatementParser.parseAnalyze();
        System.err.println(parseAnalyze);
    }

    /**
     * AST
     */
    @Test
    public void testParserCreateDB(){
        String sql = "create database bizproduce";
        MySqlStatementParser sqlStatementParser = new MySqlStatementParser(sql);

        // SQLStatement contain any data operation
        SQLStatement sqlStatement = sqlStatementParser.parseStatement();
        System.err.println(sqlStatement);
    }
}

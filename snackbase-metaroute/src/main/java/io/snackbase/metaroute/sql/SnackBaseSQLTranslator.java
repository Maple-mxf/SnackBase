package io.snackbase.metaroute.sql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

/**
 * {@link SnackBaseSQLTranslator} translator sql language;default use MySQL sql language,
 * Not support other SQL
 * <p>
 * <p>
 * base on alibaba druid
 *
 * <p>
 * <p>
 * {@link MySqlSchemaStatVisitor}
 * {@link com.alibaba.druid.sql.SQLUtils} AST parse
 * ===================================================================
 *
 *
 * <p>
 * all sql action(function,condition,expr,order by is a sql Object by druid)
 * {@link com.alibaba.druid.sql.ast.SQLObject}
 * {@link SQLStatement} has all create,drop,delete,update,alt,insert statement
 *
 * @author maxuefeng
 * @se
 * @see MySqlStatementImpl all MySQL statement paranet class
 * <p>
 * =======================================================================
 * database
 *
 * <code>show databases</code>
 * @see com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement
 *
 * <code>create database t1 ...</code>
 * @see com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement  is generic,any databases
 *
 * <code>drop database t1</code>
 * @see com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement is generic,any databases
 * <p>
 * database
 * ========================================================================
 * <p>
 * <p>
 * =========================================================================
 * table
 * <code>create table student () comment ''; </code>
 * @see com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement
 * <p>
 * {}
 * <code>alt table</code>
 * @see com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn
 *
 * <code>drop table</code>
 * @see com.alibaba.druid.sql.ast.statement.SQLDropTableStatement
 *
 * <code>select table</code>
 * @see com.alibaba.druid.sql.ast.statement.SQLSelectStatement is generic, any databases but no PG database
 *
 * <code>delete line</code>
 * @see com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement
 * <p>
 * <p>
 * =========================================================================
 * transaction
 *
 * <code>commit</code>
 * TODO   completed the transaction sql code
 * @see com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement
 * @since 1.0
 */
final
class SnackBaseSQLTranslator {

    public void translator(String sql) {
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        // database level
        if (sqlStatement instanceof SQLDropDatabaseStatement) {

        } else if (sqlStatement instanceof MySqlShowCreateDatabaseStatement) {

        } else if (sqlStatement instanceof SQLCreateDatabaseStatement) {

        } else if (sqlStatement instanceof MySqlCreateTableStatement) {

        } else if (sqlStatement instanceof MySqlAlterTableAlterColumn) {

        } else if (sqlStatement instanceof SQLDropTableStatement) {

        } else if (sqlStatement instanceof SQLSelectStatement) {

        } else if (sqlStatement instanceof MySqlDeleteStatement) {

        } else if (sqlStatement instanceof MySqlSelectParser) {

        }
    }
}

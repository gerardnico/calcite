# Calcite experimentation


## Main

  * Doc Index: [Background](src/main/java/com/gerardnico/calcite/Background.java)
  * Programmatic schema created with reflexion [SchemaReflective](src/main/java/com/gerardnico/calcite/SchemaReflective.java)
  * RelBuilder example is also working at [RelBuilderExample](src/main/java/com/gerardnico/calcite/RelBuilderExample.java)
  * Relational expression > Sql > Execute and print against a Jdbc data store. [JdbcRelToSql](src/test/java/com/gerardnico/calcite/JdbcRelToSql.java)
  
## FYI

This code is an extract of the test core project of calcite.

The database used is `Hsqldb` and the data file `SCOTT` is also in a jar file.

##

AbstractQueryableTable is the Abstract base class for implementing {@link org.apache.calcite.schema.Table}.

org.apache.calcite.linq4j.QueryProvider interface defines 
methods to create and execute queries that are described by a {@link Queryable} object.

Enumerator (org.apache.calcite.linq4j.Enumerator) is the cursor

explain plan sql
"explain plan " + extra + "for " + sql

See:
org.apache.calcite.test.SqlToRelTestBase#convertSqlToRel

public SqlNode parseQuery(String sql) throws Exception {
      final SqlParser.Config config =
          SqlParser.configBuilder().setConformance(getConformance()).build();
      SqlParser parser = SqlParser.create(sql, config);
      return parser.parseQuery();
    }
    
RelRoot is the root of the tree
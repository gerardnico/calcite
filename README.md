# Calcite experimentation


## Main

Demos:

  * A query (The first demo of the doc): [HelloWorld](src/main/java/com/gerardnico/calcite/HelloWorld.java)
  * From `Relational expression` to `Sql` then Execute and print against a Jdbc data store. [JdbcRelToSql](src/test/java/com/gerardnico/calcite/JdbcRelToSql.java)
  * `Sql Parse Tree` demo [SqlParserTest](src/test/java/com/gerardnico/calcite/SqlParserTest.java)
      * Sql Pretty Print
      * Sql validation
  * Programmatic schema created with reflexion [SchemaReflective](src/main/java/com/gerardnico/calcite/SchemaReflective.java)
  * `Catalog Reader` that shows the scheam and tables [CatalogsTest](src/test/java/com/gerardnico/calcite/CatalogsTest.java)
  * `Relation Expression Builder` example is also working at [RelBuilderExample](src/main/java/com/gerardnico/calcite/RelBuilderExample.java)
  
## FYI

This code is an extract of the test core project of calcite.

The database used is `Hsqldb` and the data file `SCOTT` is also in a jar file.

## Environement

  * [Connection]()
            final SchemaPlus schema = createSchema(modelPath);
            final FrameworkConfig frameworkConfig = createFrameworkConfig(schema);
            final Planner planner = Frameworks.getPlanner(frameworkConfig);

AbstractQueryableTable is the Abstract base class for implementing {@link org.apache.calcite.schema.Table}.

org.apache.calcite.linq4j.QueryProvider interface defines 
methods to create and execute queries that are described by a {@link Queryable} object.

Enumerator (org.apache.calcite.linq4j.Enumerator) is the cursor

explain plan sql
"explain plan " + extra + "for " + sql

See:
org.apache.calcite.test.SqlToRelTestBase#convertSqlToRel


    
RelRoot is the root of the tree
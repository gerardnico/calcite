# Calcite experimentation


## Demos

  * [HelloWorld](src/main/java/com/gerardnico/calcite/demo/HelloWorld.java) - A query (The first demo of the doc)
  * [Query Planning process](src/test/java/com/gerardnico/calcite/CalciteFrameworksTest.java) - parse sql, validate, transform to relation expression and execute with a [Planner](https://github.com/apache/calcite/blob/master/core/src/main/java/org/apache/calcite/tools/Planner.java)

## Other demos
 
  * [Sql Select Info](src/test/java/com/gerardnico/calcite/CalciteSqlSelectTest.java) - Parse a Select Sql and extracts tokens
  * [Sql Pretty Print](src/test/java/com/gerardnico/calcite/CalciteSqlWriterTest.java) - Parse a SQL to SqlNode and print it pretty
  * [Sql Validation](src/test/java/com/gerardnico/calcite/CalciteSqlValidationTest.java) - Parse a SQL to SqlNode and validate it
  * [Relational Expression](src/test/java/com/gerardnico/calcite/CalciteRelExpressionTest.java) - shows how to create several relational expression, print the sql and execute them
  * [Relational Expression from Jdbc Schema](src/test/java/com/gerardnico/calcite/CalciteRelJdbcTest.java) From RelBuilder based on a Jdbc data store, build a relational expression and transform it to SQL.       
  * [Schema](src/test/java/com/gerardnico/calcite/CalciteSchemaTest.java) - Shows how to create a schema from Java Object via reflexion
  * [Catalog Reader](src/test/java/com/gerardnico/calcite/CalciteCatalogsTest.java) - read the schema and tables from a catalog 
   
  
## FYI

  * This code is an extract and adaptation of the test core project of calcite.
  * The database used are `Hsqldb` and `H2` and the data files such as `SCOTT` are also in a jar dependency file.
  
## Doc / Reference

  * https://github.com/michaelmior/calcite-notebooks




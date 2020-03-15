# Calcite experimentation


## Demos

  * [HelloWorld](src/main/java/com/gerardnico/calcite/demo/HelloWorld.java) - A query (The first demo of the doc)
  * [Relational Expression To Sql](src/test/java/com/gerardnico/calcite/CalciteRelToSqlTest.java) From RelBuilder based on a Jdbc data store, build a relational expression and transform it to SQL. 
  * [Sql Validation](src/test/java/com/gerardnico/calcite/CalciteSqlValidationTest.java) - Parse a SQL to SqlNode and validate it
  * [Sql Pretty Print](src/test/java/com/gerardnico/calcite/CalciteSqlWriterTest.java) - Parse a SQL to SqlNode and print it pretty      
  * [SchemaReflective](src/test/java/com/gerardnico/calcite/CalciteSchemaReflectiveTest.java) - Shows how to create a schema from Java Object via reflexion
  * [Catalog Reader](src/test/java/com/gerardnico/calcite/CalciteCatalogsTest.java) - read the schema and tables from a catalog 
  * [Relational Expression](src/test/java/com/gerardnico/calcite/CalciteRelExpressionTest.java) - shows how to create several relational expression and execute them 
  
## FYI

  * This code is an extract and adaptation of the test core project of calcite.
  * The database used are `Hsqldb` and `H2` and the data files such as `SCOTT` are also in a jar dependency file.




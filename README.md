# Calcite Demo's

[![Build Status](https://travis-ci.org/gerardnico/calcite.svg?branch=master)](https://travis-ci.org/gerardnico/calcite)

## About

A repository of [Calcite](https://calcite.apache.org/) demo's 


> Note: This demos are using a lot of [Reflective Schema](src/test/java/com/gerardnico/calcite/CalciteSchemaTest.java) 
> (ie schema in the code) but in practice, you would connect to a data store (database, adapter) to get a schema.


## Getting Started Demos

  * [Getting Started Article](https://gerardnico.com/db/calcite/getting_started) 
    * [Getting Started Code](src/test/java/com/gerardnico/calcite/CalciteFrameworksTest.java) - parse sql, validate, transform to relation expression and execute with a [Planner](https://github.com/apache/calcite/blob/master/core/src/main/java/org/apache/calcite/tools/Planner.java)

  * [HelloWorld of the official documentation](https://calcite.apache.org/docs/index.html)
    * [HelloWorld Code](src/main/java/com/gerardnico/calcite/demo/HelloWorld.java) - A query (The first demo of the doc)
  
## Demo Schema and Catalog
        
  * [Reflective Schema](src/test/java/com/gerardnico/calcite/CalciteSchemaTest.java) - Shows how to create a schema from Java Object via reflexion
  * [Catalog Reader](src/test/java/com/gerardnico/calcite/CalciteCatalogsTest.java) - read the schema and tables from a catalog

## Demo Sql Parsing and validation
 
Select Statement:
  * [Sql Select Info](src/test/java/com/gerardnico/calcite/CalciteSqlSelectTest.java) - Parse a Select Sql and extracts tokens
  * [Sql Select Visitor](src/test/java/com/gerardnico/calcite/CalciteSqlVisitorTest.java) - Parse a Select Sql and build the tree (SqlNode) and visit it
  * [Sql Pretty Print](src/test/java/com/gerardnico/calcite/CalciteSqlWriterTest.java) - Parse a SQL to SqlNode and print it pretty
  * [Sql Validation](src/test/java/com/gerardnico/calcite/CalciteSqlValidationTest.java) - Parse a SQL to SqlNode and validate it

There is also other [node type](https://github.com/apache/calcite/tree/master/core/src/main/java/org/apache/calcite/sql):
  * [SqlCreate](https://github.com/apache/calcite/blob/master/core/src/main/java/org/apache/calcite/sql/SqlCreate.java)
  * [SqlInsert](https://github.com/apache/calcite/blob/master/core/src/main/java/org/apache/calcite/sql/SqlInsert.java)
  * ...


## Demo Logical Relational Expression

 * [Relational Logical Expression](src/test/java/com/gerardnico/calcite/CalciteRelLogicalExpressionTest.java) - shows how to create several relational logical expression, print the sql and execute them
 * [Relational Expression from Jdbc Schema](src/test/java/com/gerardnico/calcite/CalciteRelJdbcTest.java) From RelBuilder based on a Jdbc data store, build a relational expression and transform it to SQL.

## Demo Optimization / Cost
   
  * [Relational Expression Optimization with the HepPlanner](src/test/java/com/gerardnico/calcite/CalcitePlannerHepTest.java) - shows the filter early optimization
  * [Relational Expression Optimization  with the Volcano Planner (and cost)](src/test/java/com/gerardnico/calcite/CalcitePlannerVolcanoTest.java) - optimization with the Volcano Planner

 
  
## FYI

  * This code is an extract and adaptation of the test core project of calcite.
  * The database used are `Hsqldb` and `H2` and the data files such as `SCOTT` are also in a jar dependency file.

  
## Doc / Reference

  * [Calcite demo in notebook](https://github.com/michaelmior/calcite-notebooks)




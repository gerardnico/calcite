/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gerardnico.calcite.demo;


import com.gerardnico.calcite.CalciteConnectionStatic;
import com.gerardnico.calcite.schema.foodmart.FoodmartSchema;
import com.gerardnico.calcite.schema.hr.HrSchema;
import com.gerardnico.calcite.schema.queries.HrFoodmartQuery;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

/**
 * Example of using Calcite via JDBC.
 *
 * <p>Schema is specified programmatically via reflection on class.</p>
 * <p>
 * Copy of <a href=https://github.com/apache/calcite/blob/master/core/src/test/java/org/apache/calcite/examples/foodmart/java/JdbcExample.java>JdbcExample</a>
 */
public class SchemaReflective {


  /**
   * For more demo, see the test
   * @param args
   * @throws Exception
   */
    public static void main(String[] args) throws Exception {

        SchemaReflective schemaReflective = new SchemaReflective();

        CalciteConnection calciteConnection = schemaReflective.createConnectionWithModel();
        HrFoodmartQuery.executeSampleQuery(calciteConnection);

    }

    public CalciteConnection createConnectionWithModel() {
        return CalciteConnectionStatic.getConnectionWithModel("src/main/resources/hrAndFoodmart/hrAndFoodmart.json");
    }

    public CalciteConnection createConnectionWithSchemaInCode() {

        CalciteConnection calciteConnection = CalciteConnectionStatic.getConnectionWithoutModel();
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        rootSchema.add("hr", new ReflectiveSchema(new HrSchema()));
        rootSchema.add("foodmart", new ReflectiveSchema(new FoodmartSchema()));
        return calciteConnection;

    }


}


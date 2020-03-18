package com.gerardnico.calcite;

import com.gerardnico.calcite.schema.hr.HrSchema;
import org.apache.calcite.adapter.clone.CloneSchema;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.interpreter.Bindables;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelHomogeneousShuttle;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttle;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.logical.LogicalTableScan;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.tools.RelRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Copy and modification of the {@link org.apache.calcite.tools.RelRunners}.
 *
 * to prevent
 * java.sql.SQLException: exception while executing query: null
 * at
 * org.apache.calcite.jdbc.CalcitePrepare$CalciteSignature.enumerable(CalcitePrepare.java:355)
 *
 * because the schema is not provided
 *
 */
public class CalciteRelRunners {


    private CalciteRelRunners() {
    }

    public static PreparedStatement run(RelNode rel) {
        return run(rel, null);
    }

    /**
     * Runs a relational expression by creating a JDBC connection.
     */
    public static PreparedStatement run(RelNode rel, SchemaPlus schemaPlus) {
        final RelShuttle shuttle = new RelHomogeneousShuttle() {
            @Override
            public RelNode visit(TableScan scan) {
                final RelOptTable table = scan.getTable();
                if (scan instanceof LogicalTableScan
                        && Bindables.BindableTableScan.canHandle(table)) {
                    // Always replace the LogicalTableScan with BindableTableScan
                    // because it's implementation does not require a "schema" as context.
                    return Bindables.BindableTableScan.create(scan.getCluster(), table);
                }
                return super.visit(scan);
            }
        };
        rel = rel.accept(shuttle);
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:")) {
            /**
             * To prevent java.sql.SQLException: exception while executing query: null
             * we build the schema
             */
            if (schemaPlus != null) {
                CalciteConnection calciteConnection = (CalciteConnection) connection;
                SchemaPlus rootSchema = calciteConnection.getRootSchema();
                Schema reflectiveSchema = schemaPlus.unwrap(ReflectiveSchema.class);
                rootSchema.add(schemaPlus.getName(), reflectiveSchema);
            }
            final RelRunner runner = connection.unwrap(RelRunner.class);
            return runner.prepare(rel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

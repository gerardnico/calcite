package com.gerardnico.calcite.schema.foodmart;

/**
 * Object that will be used via reflection to create the "foodmart"
 * schema.
 */
public class FoodmartSchema {

    /**
     * The static class for the `
     * @return
     */
    public static FoodmartSchema createFoodmartSchema(){
        return new FoodmartSchema();
    }

    public final SalesFact[] sales_fact_1997 = {
            new SalesFact(100, 10),
            new SalesFact(150, 20),
    };


    /**
     * Object that will be used via reflection to create the
     * "sales_fact_1997" fact table.
     */
    public static class SalesFact {
        public final int cust_id;
        public final int prod_id;

        public SalesFact(int cust_id, int prod_id) {
            this.cust_id = cust_id;
            this.prod_id = prod_id;
        }
    }

}

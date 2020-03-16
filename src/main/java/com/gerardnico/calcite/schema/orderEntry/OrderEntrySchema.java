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
package com.gerardnico.calcite.schema.orderEntry;

/**
 * A Schema representing OrderEntry.
 *
 * <p>The Schema is meant to be used with
 * {@link org.apache.calcite.adapter.java.ReflectiveSchema} thus all
 * fields, and methods, should be public.
 */
public final class OrderEntrySchema {

    public final Customers[] CUSTOMERS = {
            new Customers(1),
            new Customers(2)
    };

    public final OrderItems[] ORDER_ITEMS = {
            new OrderItems(1,2),
            new OrderItems(2,1)
    };

    public final Orders[] ORDERS = {
            new Orders(1),
            new Orders(2)
    };

    public final Products[] PRODUCTS = {
            new Products(1),
            new Products(2)
    };

    /**
     * Orders
     */
    public static class OrderItems {
        public final int ORDER_ID;
        public final int PRODUCT_ID;

        public OrderItems(int orderId, int productId) {
            ORDER_ID = orderId;
            PRODUCT_ID = productId;
        }
    }

    /**
     * Orders
     */
    public static class Orders {
        public final int ORDER_ID;


        public Orders(int orderId) {
            ORDER_ID = orderId;
        }
    }

    /**
     * Customers
     */
    public static class Customers {
        public final int CUSTOMER_ID;
        public int ORDER_ID;


        public Customers(int customerId) {
            CUSTOMER_ID = customerId;
        }
    }

    /**
     * Products
     */
    public static class Products {
        public final int PRODUCT_ID;


        public Products(int productId) {
            PRODUCT_ID = productId;
        }
    }

}

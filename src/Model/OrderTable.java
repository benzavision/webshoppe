package Model;

/**
 * Created by Robin on 2015-10-01.
 *
 * Queries related to orders, contains mappings in/out for prepared statements.
 */

abstract class OrderTable {
    class CreateOrder {
        public static final String QUERY = "INSERT INTO `order` (owner, created, status, changed) VALUES (?, ?, 1, ?);";

        class IN {
            public static final int OWNER = 1;
            public static final int CREATED = 2;
            public static final int CHANGED = 3;
        }

        class OUT {
            public static final int ORDER_ID = 1;
        }
    }

    class CopyFromCart {
        public static final String QUERY = "INSERT INTO order_product (product, `order`, count)" +
                "SELECT product, ?, count FROM cart WHERE owner = ?;";

        class IN {
            public static final int ORDER = 1;
            public static final int OWNER = 2;
        }
    }

    /**
     * Uses GetOrder for OUT.
     */
    class GetOrders {
        public static final String QUERY =
                "SELECT `order`.id, `order`.created, `order`.changed, orderstatus.name " +
                        "FROM `order`, orderstatus " +
                        "WHERE orderstatus.id = `order`.status " +
                        "AND owner = ?;";

        class IN {
            public static final int OWNER = 1;
        }
    }

    class GetOrder {
        public static final String QUERY =
                "SELECT `order`.id, `order`.created, `order`.changed, orderstatus.name " +
                        "FROM `order`, orderstatus " +
                        "WHERE orderstatus.id = `order`.status " +
                        "AND `order`.id = ? " +
                        "AND `order`.owner = ?;";

        class IN {
            public static final int ORDER_ID = 1;
            public static final int OWNER_ID = 2;
        }

        class OUT {
            public static final int ORDER_ID = 1;
            public static final int CREATED = 2;
            public static final int CHANGED = 3;
            public static final int STATUS = 4;
        }
    }

    class GetOrderItems {
        public static final String QUERY =
                "SELECT product.name, order_product.count, product.cost, product.id FROM order_product, product WHERE " +
                        "order_product.order = ? " +
                        "AND order_product.product = product.id;";

        class IN {
            public static final int ORDER_ID = 1;
        }

        class OUT {
            public static final int NAME = 1;
            public static final int COUNT = 2;
            public static final int COST = 3;
            public static final int PRODUCT_ID = 4;
        }
    }

    class ClearOrders {
        public static final String CLEAR_ORDERS = "DELETE FROM `order` WHERE owner = ?;";
        public static final String CLEAR_ORDERS_ITEMS = "DELETE FROM order_product WHERE " +
                "`order` IN (SELECT id FROM `order` WHERE owner = ?);";

        class IN {
            public static final int OWNER_ID = 1;
        }
    }

    class GetOrderForShipping {
        public static final String QUERY = "" +
                "SELECT * FROM `order` WHERE status = 1 " +
                "AND `order`.id " +
                "NOT IN (" +
                "SELECT order_product.`order` " +
                "FROM order_product, product " +
                "WHERE order_product.count > product.count " +
                "AND order_product.product = product.id) " +
                "LIMIT 1;";

        class OUT {
            public static final int ORDER_ID = 1;
            public static final int OWNER_ID = 2;
            public static final int CREATED = 3;
        }
    }

    class SetOrderStatus {
        public static final String QUERY = "" +
                "UPDATE `order` SET `order`.status = ?, `order`.changed = ? WHERE `order`.id = ?;";

        class IN {
            public static final int STATUS = 1;
            public static final int CHANGED = 2;
            public static final int ORDER_ID = 3;
        }
    }

    class DeductStockByOrder {
        public static final String QUERY = "" +
                "UPDATE product SET count = count - (" +
                "SELECT count FROM order_product " +
                "WHERE `order` = ? " +
                "AND order_product.product = product.id) " +
                "WHERE product.id IN (SELECT product FROM order_product " +
                "WHERE order_product.`order` = ?)";

        class IN {
            public static final int ORDER_ID1 = 1;
            public static final int ORDER_ID2 = 2;
        }
    }
}

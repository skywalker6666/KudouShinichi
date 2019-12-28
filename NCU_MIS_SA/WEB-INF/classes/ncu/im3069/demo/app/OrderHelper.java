package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class OrderHelper {
    
    private static OrderHelper oh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private OrderProductHelper oph =  OrderProductHelper.getHelper();
    
    private OrderHelper() {
    }
    
    public static OrderHelper getHelper() {
        if(oh == null) oh = new OrderHelper();
        
        return oh;
    }
    
    public JSONObject create(Order order) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        int idtbl_order = -1;
        JSONArray opa = new JSONArray();
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`tbl_order`(`memberID`, `buyer_name`, `ship_address`, `cellphone`,`product_delivery`,`payment`,`order_status`,`total_price`, `create`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int memberID = order.getMemberID();
            String buyer_name = order.getBuyerName();           
            String ship_address = order.getShipAddress();
            String cellphone = order.getCellphone();
            String product_delivery= order.getProductDelivery();
            String payment= order.getPayment();
            int order_status= order.getOrderStatus();
            int total_price = order.getTotalPrice();
            Timestamp create = order.getCreateTime();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, memberID);
            pres.setString(2, buyer_name);
            pres.setString(3, ship_address);
            pres.setString(4, cellphone);
            pres.setString(5, product_delivery);
            pres.setString(6, payment);
            pres.setInt(7, order_status);
            pres.setInt(8, total_price);
            pres.setTimestamp(9, create);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            ResultSet rs = pres.getGeneratedKeys();

            if (rs.next()) {
                idtbl_order = rs.getInt(1);
                ArrayList<OrderProduct> opd = order.getOrderProduct();
                opa = oph.createByList(idtbl_order, opd);
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("order_id", idtbl_order);
        response.put("order_product_id", opa);

        return response;
    }
    
    public JSONObject getAll() {
        Order o = null;
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`tbl_order`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int idtbl_order = rs.getInt("idtbl_order");
                int memberID = rs.getInt("memberID");
                String buyer_name = rs.getString("buyer_name");
                String ship_address = rs.getString("ship_address");
                String cellphone = rs.getString("cellphone");
                String product_delivery = rs.getString("product_delivery");
                String payment = rs.getString("payment");
                int order_status=rs.getInt("order_status");
                int total_price=rs.getInt("_total_price");
                Timestamp create = rs.getTimestamp("create");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                o = new Order(idtbl_order, memberID, buyer_name, ship_address, cellphone, product_delivery, payment, order_status, total_price, create);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getOrderAllInfo());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject getById(String order_id) {
        JSONObject data = new JSONObject();
        Order o = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`tbl_order` WHERE `tbl_order`.`idtbl_order` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, order_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int idtbl_order = rs.getInt("idtbl_order");
                int memberID = rs.getInt("memberID");
                String buyer_name = rs.getString("buyer_name");
                String ship_address = rs.getString("ship_address");
                String cellphone = rs.getString("cellphone");
                String product_delivery =rs.getString("product_delivery");
                String payment =rs.getString("payment");
                int order_status =rs.getInt("order_status");
                int total_price = rs.getInt("total_price");
                Timestamp create = rs.getTimestamp("create");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                o = new Order(idtbl_order, memberID, buyer_name, ship_address, cellphone, product_delivery, payment, order_status, total_price, create);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                data = o.getOrderAllInfo();
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", data);

        return response;
    }
}

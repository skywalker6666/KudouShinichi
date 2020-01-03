package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class OrderProductHelper {
    
    private OrderProductHelper() {
        
    }
    
    private static OrderProductHelper oph;
    private Connection conn = null;
    private PreparedStatement pres = null;
//    JSONArray jsa = new JSONArray();
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static OrderProductHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(oph == null) oph = new OrderProductHelper();
        
        return oph;
    } 
    
    public JSONArray createByList(int orderID, List<OrderProduct> orderproduct) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < orderproduct.size() ; i++) {
            OrderProduct op = orderproduct.get(i);
            
            /** 取得所需之參數 */
            int productID = op.getProduct().getID();
            int sellerID = op.getProduct().getSellerID();
            int unit_price = op.getPrice();
            int product_quantities = op.getQuantity();
            double subtotal = op.getSubTotal();
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO `missa`.`tbl_orderproduct`(`orderID`, `productID`, `sellerID`, `unit_price`, `product_quantities`, `subtotal`)"

                        + " VALUES(?, ?, ?, ?, ?,?)";

                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setLong(1, orderID);
                pres.setInt(2, productID);
                pres.setInt(3, sellerID);
                pres.setInt(4, unit_price);
                pres.setInt(5, product_quantities);
                pres.setDouble(6, subtotal);
                
                /** 執行新增之SQL指令並記錄影響之行數 */
                pres.executeUpdate();
                
                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                ResultSet rs = pres.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    jsa.put(id);
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
        }
        
        return jsa;
    }
    
    public ArrayList<OrderProduct> getOrderProductByOrderId(int orderID) {
        ArrayList<OrderProduct> result = new ArrayList<OrderProduct>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        OrderProduct op;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`tbl_orderproduct` WHERE `tbl_orderproduct`.`orderID` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, orderID);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int idtbl_orderproduct= rs.getInt("idtbl_orderproduct");
                int productID = rs.getInt("productID");
                int sellerID = rs.getInt("sellerID");
                int unit_price = rs.getInt("unit_price");
                int product_quantities = rs.getInt("product_quantities");
                double subtotal = rs.getDouble("subtotal");
                
                /** 將每一筆會員資料產生一名新orderproduct物件 */
                op = new OrderProduct(idtbl_orderproduct, orderID, productID, sellerID, unit_price, product_quantities, subtotal);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                result.add(op);
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
        
        return result;
    }
    public JSONObject getAllHotProduct() {
    //    ArrayList<OrderProduct> result = new ArrayList<OrderProduct>();
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        Product pt;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Select idtbl_product,product_Name,price,is_deleted,image,product_info, count(*) as Total \n" + 
            		"From `missa`.tbl_orderproduct \n" + 
            		"JOIN  tbl_product ON idtbl_product=productID and tbl_product.sellerID=tbl_orderproduct.sellerID\n" + 
            		"Group By productID \n" + 
            		"Having Total>5;";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
      //      pres.setInt(1, orderID);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
      //      System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int idtbl_product= rs.getInt("idtbl_product");
                String product_Name = rs.getString("product_Name");
                int price = rs.getInt("price");
                int is_Deleted = rs.getInt("is_Deleted");
                String image=rs.getString("image");
                String product_info=rs.getString("product_info");
                int Total = rs.getInt("Total");

                System.out.println(idtbl_product);
                System.out.println(product_Name);
                System.out.println(price);
                System.out.println(is_Deleted);
                System.out.println(image);
                System.out.println(product_info);
                System.out.println(Total);
                /** 將每一筆會員資料產生一名新orderproduct物件 */
                pt = new Product(idtbl_product, product_Name, price,is_Deleted, image, product_info, Total);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(pt.getData());

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
        JSONObject response = new JSONObject();

        
        response.put("sql", exexcute_sql);

        response.put("data", jsa);
        return response;
        

    }
}

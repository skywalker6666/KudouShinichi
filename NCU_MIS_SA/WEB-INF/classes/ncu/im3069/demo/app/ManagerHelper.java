package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class managerHelper<br>
 * managerHelper類別（class）主要管理所有與manager相關與資料庫之方法（method）
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class ManagerHelper {
    
    /**
     * 實例化（Instantiates）一個新的（new）managerHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private ManagerHelper() {
        
    }
    
    /** 靜態變數，儲存managerHelper物件 */
    private static ManagerHelper mgh;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個managerHelper物件
     *
     * @return the helper 回傳managerHelper物件
     */
    public static ManagerHelper getHelper() {
        /** Singleton檢查是否已經有managerHelper物件，若無則new一個，若有則直接回傳 */
        if(mgh == null) mgh = new ManagerHelper();
        
        return mgh;
    }
    
    /**
     * 透過管理員編號（ID）刪除管理員
     *
     * @param id 管理員編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteByID(int id) {
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
            String sql = "Update `missa`.`tbl_manager` SET `isDeleted` = ?   WHERE `idtbl_manager` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, 1);
            pres.setInt(2, id);
            
            /** 執行刪除之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
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
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
    
    /**
     * 取回所有管理員資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一個 manager 物件之 m 變數，用於紀錄每一位查詢回之管理員資料 */
     Manager mg = null;
        /** 用於儲存所有檢索回之管理員，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`tbl_manager` WHERE `isDeleted` <> 1";
            
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
                int manager_id = rs.getInt("idtbl_manager");
                String managerName = rs.getString("managerName");
         //       String managerAccountName = rs.getString("managerAccountName");
                String managerPassword = rs.getString("managerPassword");
          //      String headSticker = rs.getString("headSticker");
         //       String birthday = rs.getString("birthday");
                int isLeader = rs.getInt("isLeader");
                int isDeleted = rs.getInt("isDeleted");
         //       int login_times = rs.getInt("login_times");
          //      String status = rs.getString("status");
                
                /** 將每一筆管理員資料產生一名新manager物件 */
                mg = new Manager(manager_id, managerName,  managerPassword, isLeader, isDeleted);
                /** 取出該名管理員之資料並封裝至 JSONsonArray 內 */
                jsa.put(mg.getData());
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
        
        /** 將SQL指令、花費時間、影響行數與所有管理員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 透過管理員編號（ID）取得管理員資料
     *
     * @param id 管理員編號
     * @return the JSON object 回傳SQL執行結果與該管理員編號之管理員資料
     */
    public JSONObject getByID(String id) {
        /** 新建一個 manager 物件之 m 變數，用於紀錄每一位查詢回之管理員資料 */
        Manager m = null;
        /** 用於儲存所有檢索回之管理員，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`tbl_manager` WHERE idtbl_manager = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該管理員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int manager_id = rs.getInt("idtbl_manager");
                String managerName = rs.getString("managerName");
          //      String email = rs.getString("email");
                String managerPassword = rs.getString("managerPassword");
          //      String headSticker = rs.getString("headSticker");
          //      String birthday = rs.getString("birthday");
                int isLeader = rs.getInt("isLeader");
            //    int login_times = rs.getInt("login_times");
                int isDeleted = rs.getInt("isDeleted");
                
                /** 將每一筆管理員資料產生一名新manager物件 */
                m = new Manager(manager_id, managerName, managerPassword, isLeader, isDeleted);
                /** 取出該名管理員之資料並封裝至 JSONsonArray 內 */
                jsa.put(m.getData());
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
        
        /** 將SQL指令、花費時間、影響行數與所有管理員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    public JSONObject getByNamePassword(String name, String password) {
        /** 新建一個 manager 物件之 m 變數，用於紀錄每一位查詢回之管理員資料 */
        Manager mg = null;
        /** 用於儲存所有檢索回之管理員，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`tbl_manager` WHERE `managerName` = ? AND `managerPassword` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, name);
            pres.setString(2, password);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該管理員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int manager_id = rs.getInt("idtbl_manager");
                String managerName = rs.getString("managerName");
          //      String email = rs.getString("email");
                String managerPassword = rs.getString("managerPassword");
          //      String headSticker = rs.getString("headSticker");
          //      String birthday = rs.getString("birthday");
                int is_Leader = rs.getInt("isLeader");
            //    int login_times = rs.getInt("login_times");
                int is_Deleted = rs.getInt("isDeleted");
                
                /** 將每一筆管理員資料產生一名新manager物件 */
                mg = new Manager(manager_id, managerName, managerPassword, is_Leader, is_Deleted);
                /** 取出該名管理員之資料並封裝至 JSONsonArray 內 */
                jsa.put(mg.getData());
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
        
        /** 將SQL指令、花費時間、影響行數與所有管理員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 透過管理員編號（ID）取得管理員資料
     *
     * @param id 管理員編號
     * @return the JSON object 回傳SQL執行結果與該管理員編號之管理員資料
     */
    public JSONObject getByIsLeader(String isLeader) {
        /** 新建一個 manager 物件之 m 變數，用於紀錄每一位查詢回之管理員資料 */
        Manager m = null;
        /** 用於儲存所有檢索回之管理員，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`tbl_manager` WHERE isSeller = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, isLeader);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該管理員編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int manager_id = rs.getInt("idtbl_manager");
                String managerName = rs.getString("mangerName");
          //      String email = rs.getString("email");
                String managerPassword = rs.getString("managerPassword");
          //      String headSticker = rs.getString("headSticker");
          //      String birthday = rs.getString("birthday");
                int is_Leader = rs.getInt("isLeader");
            //    int login_times = rs.getInt("login_times");
                int isDeleted = rs.getInt("isDeleted");
                
                /** 將每一筆管理員資料產生一名新manager物件 */
                m = new Manager(manager_id, managerName, managerPassword, is_Leader, isDeleted);
                /** 取出該名管理員之資料並封裝至 JSONsonArray 內 */
                jsa.put(m.getData());
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
        
        /** 將SQL指令、花費時間、影響行數與所有管理員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    

    
    /**
     * 取得該名管理員之更新時間與所屬之管理員組別
     *
     * @param m 一名管理員之manager物件
     * @return the JSON object 回傳該名管理員之更新時間與所屬組別（以JSONObject進行封裝）
     */
    public JSONObject getLoginTimesStatus(Manager m) {
        /** 用於儲存該名管理員所檢索之更新時間分鐘數與管理員組別之資料 */
        JSONObject jso = new JSONObject();
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`tbl_manager` WHERE idtbl_manager = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, m.getID());
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該電子郵件之資料，因此其實可以不用使用 while迴圈 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
                int login_times = rs.getInt("login_times");
                String status = rs.getString("status");
                /** 將其封裝至JSONObject資料 */
                jso.put("login_times", login_times);
                jso.put("status", status);
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

        return jso;
    }
    
    /**
     * 檢查該名管理員之名稱是否重複註冊
     *
     * @param m 一名管理員之manager物件
     * @return boolean 若重複註冊回傳False，若該名稱不存在則回傳True
     */
    public boolean checkDuplicate(Manager mg){
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `missa`.`tbl_manager` WHERE managerName = ?";

            /** 取得所需之參數 */
            String managerName = mg.getmanagerName();
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, managerName);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            System.out.print(row);

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
        
        /** 
         * 判斷是否已經有一筆該電子郵件信箱之資料
         * 若無一筆則回傳False，否則回傳True 
         */
        return (row == 0) ? false : true;
    }

    
    /**
     * 檢查該名會員之電子郵件信箱是否重複註冊
     *
     * @param m 一名會員之Member物件
     * @return boolean 若重複註冊回傳False，若該信箱不存在則回傳True
     */
    public boolean checkIfHasThisAccount(Manager mg){
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `missa`.`tbl_manager` WHERE `managerName` = ?  AND `managerPassword` = ? LIMIT 1";
            
            /** 取得所需之參數 */
            String name = mg.getmanagerName();
            String password = mg.getPassword();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, name);
            pres.setString(2, password);
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            System.out.println("取得"+row+"筆資料");

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL ate: sdss%s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
        	
            DBMgr.close(rs, pres, conn);
        }
        
        /** 
         * 判斷是否已經有一筆該電子郵件信箱之資料
         * 若無一筆則回傳False，否則回傳True 
         */
        return (row == 0) ? false : true;
    }
    
    
    /**
     * 建立該名管理員至資料庫
     *
     * @param m 一名管理員之manager物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Manager mg) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`tbl_manager` (`managerName`, `managerPassword`, `isLeader`, `isDeleted`)"
                    + " VALUES(?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            String name = mg.getmanagerName();
            String password = mg.getPassword();
            int isLeader = mg.getIsLeader();
            int isDeleted = mg.getisDeleted();
            
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, name);
            pres.setString(2, password);
            pres.setInt(3, isLeader); 
            pres.setInt(4, isDeleted);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

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

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("time", duration);
        response.put("row", row);

        return response;
    }
    
    public JSONObject update(Manager mg) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`tbl_manager` SET managerName = ? ,`managerPassword` = ?  WHERE idtbl_manager = ?";
            /** 取得所需之參數 */
            int idtbl_manager = mg.getID();
            String managerName = mg.getmanagerName();
            String password = mg.getPassword();
            int isLeader = mg.getIsLeader();
            int isDeleted = mg.getisDeleted();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, managerName);
            pres.setString(2, password);
            pres.setInt(3, idtbl_manager);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

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
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 更新管理員更新資料之分鐘數
     *
     * @param m 一名管理員之manager物件
     */
//   public void updateLoginTimes(Manager m) {
////        /** 更新時間之分鐘數 */
//        int new_times = mg.getLoginTimes();
////        
////        /** 記錄實際執行之SQL指令 */
//        String exexcute_sql = "";
//        
//        try {
////            /** 取得資料庫之連線 */
//            conn = DBMgr.getConnection();
//            /** SQL指令 */
//            String sql = "Update `missa`.`tbl_manager` SET login_times = ? WHERE idtbl_manager = ?";
////            /** 取得管理員編號 */
//            int id = m.getID();
////            
////            /** 將參數回填至SQL指令當中 */
//            pres = conn.prepareStatement(sql);
//            pres.setInt(1, new_times);
//            pres.setInt(2, id);
////            /** 執行更新之SQL指令 */
//            pres.executeUpdate();
////
////            /** 紀錄真實執行的SQL指令，並印出 **/
//            exexcute_sql = pres.toString();
//            System.out.println(exexcute_sql);
////
//        } catch (SQLException e) {
////            /** 印出JDBC SQL指令錯誤 **/
//            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
//        } catch (Exception e) {
//            /** 若錯誤則印出錯誤訊息 */
//            e.printStackTrace();
//        } finally {
//            /** 關閉連線並釋放所有資料庫相關之資源 **/
//            DBMgr.close(pres, conn);
//      }
//    }
//    
    /**
     * 更新管理員之管理員組別
     *
     * @param m 一名管理員之manager物件
     * @param status 管理員組別之字串（String）
     */
//    public void updateStatus(Manager m, String status) {      
//        /** 記錄實際執行之SQL指令 */
//        String exexcute_sql = "";
//        
//        try {
//            /** 取得資料庫之連線 */
//            conn = DBMgr.getConnection();
//            /** SQL指令 */
//            String sql = "Update `missa`.`tbl_manager` SET status = ? WHERE idtbl_manager = ?";
//            /** 取得管理員編號 */
//            int id = m.getID();
//            
//            /** 將參數回填至SQL指令當中 */
//            pres = conn.prepareStatement(sql);
//            pres.setString(1, status);
//            pres.setInt(2, id);
//            /** 執行更新之SQL指令 */
//            pres.executeUpdate();
//
//            /** 紀錄真實執行的SQL指令，並印出 **/
//            exexcute_sql = pres.toString();
//            System.out.println(exexcute_sql);
//        } catch (SQLException e) {
//            /** 印出JDBC SQL指令錯誤 **/
//            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
//        } catch (Exception e) {
//            /** 若錯誤則印出錯誤訊息 */
//            e.printStackTrace();
//        } finally {
//            /** 關閉連線並釋放所有資料庫相關之資源 **/
//            DBMgr.close(pres, conn);
//        }
//    }

}
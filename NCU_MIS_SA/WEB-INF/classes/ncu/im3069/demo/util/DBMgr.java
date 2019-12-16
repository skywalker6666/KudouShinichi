package ncu.im3069.demo.util;

import java.sql.*;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class DBMgr<br>
 * DBMgr類別（class）主要管理與資料庫建立與關閉連線之方法（method），並儲存相關之設定資料<br>
 * 每個要與資料庫建立連線之類別應該import本類別
 * </p>
 *
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class DBMgr {

    /** JDBC_DRIVER常數，設定JDBC驅動之類別名稱 */
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /** DB_URL常數，指定資料庫所在之IP或網域、Port號碼與指定所要使用資料庫 */
    static final String DB_URL = "jdbc:mysql://localhost:3306/missa?allowPublicKeyRetrieval=true&useSSL=false";

    /** USER常數，所要使用之資料庫使用者帳號 */
    static final String USER = "root";

    /** PASS常數，所有使用之資料庫使用者密碼 */


    static final String PASS = "";


    /** 靜態指定所要使用之Class名稱 **/
    static {
        try {
            /** 載入JDBC驅動類別，並執行初始化 */
            Class.forName(DBMgr.JDBC_DRIVER);
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        }
    }

    /**
     * 實例化（Instantiates）一個新的（new）DBMgr物件
     */
    public DBMgr() {

    }

    /**
     * 透過JDBC建立資料庫之連線
     *
     * @return the connection 回傳所建立之資料庫連線
     */
    public static Connection getConnection() {
        /** 資料庫連線相關參數 */
        Properties props = new Properties();
        /** 設定資料庫連線是否要使用SSL連線，由於預設未使用SSL連線因此本部分要指定為False */
        props.setProperty("useSSL", "false");
        /** 設定資料庫使用之時區 */
        props.setProperty("serverTimezone", "UTC");
        /** 設定是否使用Unicode，此部分要設定為True避免中文字會發生問題 */
        props.setProperty("useUnicode", "true");
        /** 設定使用之字元編碼，採用UTF-8 */
        props.setProperty("characterEncoding", "utf8");
        /** 設定連線所使用之帳號和密碼 */
        props.setProperty("user", DBMgr.USER);
        props.setProperty("password", DBMgr.PASS);

        /** 宣告Connection變數，並先指向為null */
        Connection conn = null;

        /** 嘗試透過DriverManager的getConnection()建立並取得資料庫連線 */
        try {
            conn = DriverManager.getConnection(DBMgr.DB_URL, props);
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        }

        /** 建立與取回連線成功後，回傳該連線之變數 */
        return conn;
    }

    /**
     * 關閉所有資料庫連線與釋放SQL資源
     *
     * @param stm SQL查詢之指令
     * @param conn 資料庫之連線
     */
    public static void close(Statement stm, Connection conn) {
        try {
            /** 確認Statement是否為null，若不為null則嘗試關閉Statement釋放資源 */
            if (stm != null) stm.close();
            /** 確認Connection是否為null，若不為null則嘗試關閉Connection釋放資源 */
            if (conn != null) conn.close();
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        }
    }

    /**
     * 關閉所有資料庫連線與釋放SQL資源
     *
     * @param rs 資料庫檢索後之結果資料
     * @param stm SQL查詢之指令
     * @param conn 資料庫之連線
     */
    public static void close(ResultSet rs, Statement stm, Connection conn) {
        try {
            /** 確認ResultSet是否為null，若不為null則嘗試關閉ResultSet釋放資源 */
            if (rs != null) rs.close();
            /** 確認Statement是否為null，若不為null則嘗試關閉Statement釋放資源 */
            if (stm != null) stm.close();
            /** 確認Connection是否為null，若不為null則嘗試關閉Connection釋放資源 */
            if (conn != null) conn.close();
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        }
    }

    public static String[] stringToArray(String data, String delimiter) {
      String[] result;
      result = data.split(delimiter);
      return result;
    }
}

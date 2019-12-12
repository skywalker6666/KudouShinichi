package ncu.im3069.tools;

import javax.servlet.http.*; 
import java.io.*; 
import org.json.*;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class JsonReader<br>
 * JsonReader類別（class）主要用於處理Http Rrequest和Http Rresponse之方法（method）
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class JsonReader {
    /** request，用於儲存原始Http Request */
    private HttpServletRequest request;
    
    /** string，用於儲存經讀取後之Request字串 */
    private String request_string;

    /**
     * 實例化（Instantiates）一個新的（new）JSONReader物件
     *
     * @param request Servlet Http Request物件，紀錄request
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public JsonReader(HttpServletRequest request) throws IOException, UnsupportedEncodingException {
        /** 設定 Request 之字串編碼為UTF-8，避免中文字出現錯誤 */
        request.setCharacterEncoding("UTF-8");
        /** 儲存原始Request之物件 */
        this.request = request;
        
        /** 建立一個StringBuilder組出字串 */
        StringBuilder sb = new StringBuilder();
        String s;
        
        /** 
         * 透過 while 迴圈逐行讀取資料
         * 將其讀出後存入字串變數 s
         * 最終append到StringBuilder當中
         */
        while((s = request.getReader().readLine()) != null) sb.append(s);
        
        /** 將StringBuilder轉換成String，並印出該字串 */
        this.request_string = sb.toString();
        System.out.println("[@JsonReader]" + this.request_string);
    }

    /**
     * 取得GET Request內之參數（透過網址傳值）
     *
     * @param key 傳入要取得之key值
     * @return the parameter 回傳該key值之value
     */
    public String getParameter(String key) {
        /** 判斷該 key 是否存在於Request當中，若有則回傳該 key 所儲存之value */
        if (this.request.getParameter(key) != null) return this.request.getParameter(key);
        /** 若無則回傳空字串 */
        else return "";
    }

    /**
     * 將 Http Request 轉換成 JSONArray 之物件，Request之字串最外層必須為 [] Array
     *
     * @return the array 回傳轉換成的JSONArray
     */
    public JSONArray getArray() {
        /** 
         * 建立一個JSONArray物件，用以解析Request之字串
         * 所Request之字串最外層必須為 [] Array
         */
        JSONArray request_array = new JSONArray(this.request_string);
        
        /** 回傳Request之Array格式 */
        return request_array;
    }

    /**
     * 將 Http Request 轉換成 JSONObject 之物件，Request之字串最外層必須為 {} Object
     *
     * @return the object 回傳轉換成的JSONObject
     */
    public JSONObject getObject() {
        /** 
         * 建立一個JSONObject物件，用以解析Request之字串
         * 所Request之字串最外層必須為 {} Object
         */
        JSONObject request_object = new JSONObject(this.request_string);
        
        /** 回傳Request之Object格式 */
        return request_object;
    }

    /**
     * 將 JSON 格式之字串傳入，將處理完成後的資料進行Response回前端，字串最外圍必須為 {} Object
     * 採用多載（overload）方法進行
     *
     * @param resp_string 愈Response之JSON格式字串
     * @param response Servlet回傳之HttpServletResponse之Response物件
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public void response(String resp_string, HttpServletResponse response) throws IOException, UnsupportedEncodingException {
        /** 設定Response之字元編碼 */
        response.setCharacterEncoding("UTF-8");
        /** 設定Response之文件類型 */
        response.setContentType("text/html; charset=UTF-8");
        /** 將JSON格式之字串轉換成 */
        JSONObject responseObj = new JSONObject(resp_string);
        /** 取得PrintWriter準Response回前端 */
        PrintWriter out = response.getWriter();
        /** 將Response Object放入，回傳前端 */
        out.println(responseObj);
    }
    
    /**
     * 將 JSON 格式之JSONObject傳入，將處理完成後的資料進行Response回前端
     * 採用多載（overload）方法進行
     *
     * @param resp 愈Response之JSONObject Response之物件
     * @param response Servlet回傳之HttpServletResponse之Response物件
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public void response(JSONObject resp, HttpServletResponse response) throws IOException, UnsupportedEncodingException {
        /** 設定Response之字元編碼 */
        response.setCharacterEncoding("UTF-8");
        /** 設定Response之文件類型 */
        response.setContentType("text/html; charset=UTF-8");
        /** 取得PrintWriter準Response回前端 */
        PrintWriter out = response.getWriter();
        /** 將Response Object放入，回傳前端 */
        out.println(resp);
    }
    
    /**
     * 將 JSON 格式之JSONObject傳入，將處理完成後的資料進行Response回前端
     * 採用多載（overload）方法進行
     *
     * @param status_code 指定Http之狀態碼
     * @param resp 愈Response之JSONObject Response之物件
     * @param response Servlet回傳之HttpServletResponse之Response物件
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public void response(int status_code, JSONObject resp, HttpServletResponse response) throws IOException, UnsupportedEncodingException {
        /** 設定Response之字元編碼 */
        response.setCharacterEncoding("UTF-8");
        /** 設定Response之文件類型 */
        response.setContentType("text/html; charset=UTF-8");
        /** 設定狀態碼 */
        response.setStatus(status_code);
        /** 取得PrintWriter準Response回前端 */
        PrintWriter out = response.getWriter();
        /** 將Response Object放入，回傳前端 */
        out.println(resp);
    }
}
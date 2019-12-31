
package ncu.im3069.demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.demo.app.Manager;
import ncu.im3069.demo.app.ManagerHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class ManagerController<br>
 * ManagerController類別（class）主要用於處理Manager相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/manager.do")

public class ManagerController extends HttpServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** mh，ManagerHelper之物件與Manager相關之資料庫方法（Sigleton） */
    private ManagerHelper mgh =  ManagerHelper.getHelper();
    
    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
     
        String password = jso.getString("password");
        String managerName = jso.getString("name");
        int isLeader=jso.getInt("isLeader");
        int isDeleted=jso.getInt("isDeleted");

        
        /** 建立一個新的管理員物件 */
        Manager mg = new Manager( managerName , password, isLeader, isDeleted);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if( password.isEmpty() || managerName.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        /** 透過ManagerHelper物件的checkDuplicate()檢查該管理員電子郵件信箱是否有重複 */
        else if (!mgh.checkDuplicate(mg)) {
            /** 透過ManagerHelper物件的create()方法新建一個管理員至資料庫 */
            JSONObject data = mgh.create(mg);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 註冊管理員資料...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此E-Mail帳號重複！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("idtbl_manager");
      //  String isLeader =jsr.getParameter("isLeader");
    

        /** 判斷該字串是否存在，若存在代表要取回個別管理員之資料，否則代表要取回全部資料庫內管理員之資料 */
        if (id.isEmpty()) {
            /** 透過ManagerHelper物件之getAll()方法取回所有管理員之資料，回傳之資料為JSONObject物件 */
            JSONObject query = mgh.getAll();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有管理員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
//        if(!isLeader.isEmpty()){
//        	 /** 透過ManagerHelper物件的getByID()方法自資料庫取回該名管理員之資料，回傳之資料為JSONObject物件 */
//            JSONObject query = mgh.getByIsLeader(isLeader);
//            
//            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
//            JSONObject resp = new JSONObject();
//            resp.put("status", "200");
//            resp.put("message", "管理員資料取得成功");
//            resp.put("response", query);
//    
//            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
//            jsr.response(resp, response);
//        }
        if(!id.isEmpty()){
            /** 透過ManagerHelper物件的getByID()方法自資料庫取回該名管理員之資料，回傳之資料為JSONObject物件 */
            JSONObject query = mgh.getByID(id);
            System.out.println("wd");
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "管理員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("idtbl_Manager");
        
        /** 透過ManagerHelper物件的deleteByID()方法至資料庫刪除該名管理員，回傳之資料為JSONObject物件 */
        JSONObject query = mgh.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "管理員移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int managerId = jso.getInt("managerId");
        String password = jso.getString("managerPassword");
        String name = jso.getString("managerName");


        /** 透過傳入之參數，新建一個以這些參數之管理員Manager物件 */
    //    Manager mg = new Manager(managerId, password, name, isLeader, isDeleted);
        Manager mg = new Manager(managerId, name, password );
        
        
        /** 透過Manager物件的update()方法至資料庫更新該名管理員資料，回傳之資料為JSONObject物件 */
        JSONObject data = mgh.update(mg);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新管理員資料...");
        resp.put("response", data);
  //      System.out.println("hi");
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}
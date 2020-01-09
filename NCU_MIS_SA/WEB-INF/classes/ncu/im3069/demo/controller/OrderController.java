package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Order;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.app.OrderHelper;
import ncu.im3069.demo.app.OrderProductHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/order.do")
public class OrderController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /** oh，OrderHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderHelper oh =  OrderHelper.getHelper();
	
	/** oh，OrderHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderProductHelper oph =  OrderProductHelper.getHelper();

    public OrderController() {
        super();
    }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String idtbl_order = jsr.getParameter("idtbl_order");
        String buyerID = jsr.getParameter("buyerID");
        String sellerID = jsr.getParameter("sellerID");
        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!idtbl_order.isEmpty()) {
          /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
        	System.out.println("進入getByorderId");
          JSONObject query = oh.getByOrderId(idtbl_order);
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("response", query);
        }
        else if(!buyerID.isEmpty()){
        	System.out.println("進入getByMemberId");
        	JSONObject query = oh.getByMemberId(buyerID);
            resp.put("status", "200");
            resp.put("message", "單筆訂單資料取得成功");
            resp.put("response", query);
        }
        else if(!sellerID.isEmpty()) {
        	System.out.println("進入getOPBysellerId");
        	JSONObject query = oph.getOrderProductBySellerId(sellerID);
            resp.put("status", "200");
            resp.put("message", "單筆訂單資料取得成功");
            resp.put("response", query);
        	
        }
        else {
          /** 透過 orderHelper 物件之 getAll() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
        	System.out.println("進入getAll");
          JSONObject query = oh.getAll();
          resp.put("status", "200");
          resp.put("message", "所有訂單資料取得成功");
          resp.put("response", query);
        }
        
        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int memberID = jso.getInt("memberID");
        String buyer_name = jso.getString("buyer_name");
        String ship_address = jso.getString("ship_address");
        String cellphone = jso.getString("cellphone");
        String product_delivery=jso.getString("product_delivery");
        String payment=jso.getString("payment");
        int order_status=jso.getInt("order_status");
        double total_price=jso.getDouble("total_price");
        JSONArray item = jso.getJSONArray("item");
        JSONArray quantity = jso.getJSONArray("quantity");

        /** 建立一個新的訂單物件 */
        Order od = new Order (memberID, buyer_name, ship_address, cellphone, product_delivery, payment, order_status, total_price);

        /** 將每一筆訂單細項取得出來 */
        for(int i=0 ; i < item.length() ; i++) {
            String product_id = item.getString(i);
            int amount = quantity.getInt(i);
            System.out.println(product_id);

            /** 透過 ProductHelper 物件之 getById()，取得產品的資料並加進訂單物件裡 */
            Product pd = ph.getByProductId(product_id);
            od.addOrderProduct(pd, amount);
        }

        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
        JSONObject result = oh.create(od);

        /** 設定回傳回來的訂單編號與訂單細項編號 */
        od.setId((int) result.getLong("order_id"));
        od.setOrderProductId(result.getJSONArray("order_product_id"));

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "訂單新增成功！");
        resp.put("response", od.getOrderAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
	        JsonReader jsr = new JsonReader(request);
	        JSONObject jso = jsr.getObject();
	        
	        /** 取出經解析到JSONObject之Request參數 */
	        //String orderID = jsr.getParameter("orderID");
	        //String order_status = jsr.getParameter("order_status");
	        int orderID = jso.getInt("orderID");
	        int order_status=jso.getInt("order_status");

	        /** 透過傳入之參數，新建一個以這些參數之商品Product物件 */
	        
	        
	        /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
	    
	        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	        JSONObject resp = new JSONObject();
	        
	            JSONObject data = oh.updateOrderStatus(order_status,orderID);
	            System.out.println("putbyID");
	            resp.put("status", "200");
	            resp.put("message", "所有結帳之商品資料更新成功");
	            resp.put("response", data);
	        
	        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	        jsr.response(resp, response);
	    }


}
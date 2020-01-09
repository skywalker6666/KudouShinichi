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


@WebServlet("/api/orderProduct.do")
public class OrderProductController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /** oh，OrderHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderHelper oh =  OrderHelper.getHelper();
	
	private OrderProductHelper oph=OrderProductHelper.getHelper();

    public OrderProductController() {
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
      
        
        
        String idtbl_product= jsr.getParameter("idtbl_product");
        String product_Name=jsr.getParameter("product_Name");
        String price=jsr.getParameter("price");
        String is_Deleted=jsr.getParameter("is_Deleted");
        String image=jsr.getParameter("image");
        String product_info=jsr.getParameter("product_info");
        String total=jsr.getParameter("Total");
        String idtbl_order = jsr.getParameter("idtbl_order");
        String buyerID = jsr.getParameter("buyerID");
        String sellerID = jsr.getParameter("sellerID");
        JSONObject resp = new JSONObject();
        if (!sellerID.isEmpty()) {
            /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */

        	System.out.println("getByOrderId");
            JSONObject query = oh.getByOrderId(idtbl_order);
            resp.put("status", "200");
            resp.put("message", "單筆訂單資料取得成功");
            resp.put("response", query);
        }
        else {
        	System.out.println("getAllHotProduct");

	        JSONObject query=oph.getAllHotProduct();
	        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */                
	        resp.put("status", "20000");
	        resp.put("message", "所有熱門商品取得成功");
	        resp.put("response", query);
        }
        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
       
       
       
        
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

}
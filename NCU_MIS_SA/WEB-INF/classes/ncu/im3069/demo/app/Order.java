package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Order {

    /** idtbl_order，訂單編號 */
    private int idtbl_order;

    /** memberID，會員ID*/
    private int memberID;

    /** buyer_name，會員姓名 */
    private String buyer_name;

    /** ship_address，會員地址 */
    private String ship_address;

    /** cellPhone，會員手機 */
    private String cellphone;
    
    /** product_delivery，會員產品運送方式 */
    private String product_delivery;
    
    /** payment，會員產品付款方式 */
    private String payment;
    
    /** order_status，會員訂單狀態 */
    private int order_status; 
    
    /** _total_price，訂單總金額 */
    private int total_price;
    
    /** create，訂單創建時間 */
    private Timestamp create;
    
    /** list，訂單列表 */
    private ArrayList<OrderProduct> list = new ArrayList<OrderProduct>();


    
    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private OrderProductHelper oph = OrderProductHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param memberID 會員ID
     * @param buyer_name 會員姓名
     * @param ship_address 會員地址
     * @param cellphone 會員手機
     * @param product_delivery 訂單運送方式
     * @param payment 訂單付款方式
     * @param order_status 訂單狀態
     * @param total_price 總金額
     */
    public Order(int memberID, String buyer_name, String ship_address, String cellphone, String product_delivery, String payment, int order_status, int total_price) {
        this.memberID = memberID;
        this.buyer_name = buyer_name;
        this.ship_address = ship_address;
        this.cellphone = cellphone;
        this.product_delivery = product_delivery;
        this.payment = payment;
        this.order_status=order_status;
        this.total_price=total_price;
        this.create = Timestamp.valueOf(LocalDateTime.now());
        
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param memberID 會員ID
     * @param buyer_name 會員姓名
     * @param ship_address 會員地址
     * @param cellphone 會員手機
     * @param product_delivery 訂單運送方式
     * @param payment 訂單付款方式
     * @param order_status 訂單狀態
     * @param total_price 總金額
     * @param create 訂單創建時間
     */
    public Order(int idtbl_order, int memberID, String buyer_name, String ship_address, String cellphone, String product_delivery, String payment, int order_status, int total_price, Timestamp create) {
        this.idtbl_order = idtbl_order;
        this.memberID = memberID;
        this.buyer_name = buyer_name;
        this.ship_address = ship_address;
        this.cellphone = cellphone;
        this.product_delivery=product_delivery;
        this.payment=payment;
        this.order_status=order_status;
        this.total_price=total_price;
        this.create = create;
        getOrderProductFromDB();
    }

    /**
     * 新增一個訂單產品及其數量
     */
    public void addOrderProduct(Product pd, int quantity) {
        this.list.add(new OrderProduct(pd, quantity));
    }

    /**
     * 新增一個訂單產品
     */
    public void addOrderProduct(OrderProduct op) {
        this.list.add(op);
    }

    /**
     * 設定訂單編號
     */
    public void setId(int idtbl_order) {
        this.idtbl_order = idtbl_order;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getOrderID() {
        return this.idtbl_order;
    }

    /**
     * 取得訂單會員的名
     *
     * @return String 回傳訂單會員的名
     */
    public int getMemberID() {
        return this.memberID;
    }

    /**
     * 取得訂單會員的姓
     *
     * @return String 回傳訂單會員的姓
     */
    public String getBuyerName() {
        return this.buyer_name;
    }
    
   

    /**
     * 取得訂單地址
     *
     * @return String 回傳訂單地址
     */
    public String getShipAddress() {
        return this.ship_address;
    }

    /**
     
     * 取得訂單電話
     *
     * @return String 回傳訂單電話
     */
    public String getCellphone() {
        return this.cellphone;
    }

    /**
     * 取得訂單運送方式
     *
     * @return String 回傳運送方式
     */
    public String getProductDelivery() {
        return this.product_delivery;
    }
    /**
  
   * 取得訂單付款方式
     *
     * @return String 回傳付款方式
     */
    public String getPayment() {
        return this.payment;
    }
    /**
     
   * 取得訂單訂單狀態
     *
     * @return String 回傳訂單狀態
     */
    public int getOrderStatus() {
        return this.order_status;
    }
    /**
     
    * 取得訂單總金額
     *
     * @return String 回傳總金額
     */
    public int getTotalPrice() {
        return this.total_price;
    }       
    /**
     
     * 取得訂單創建時間
     *
     * @return Timestamp 回傳訂單創建時間
     */
    public Timestamp getCreateTime() {
        return this.create;
    }
    /**

     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public ArrayList<OrderProduct> getOrderProduct() {
        return this.list;
    }

    /**
     * 從 DB 中取得訂單產品
     */
    private void getOrderProductFromDB() {
        ArrayList<OrderProduct> data = oph.getOrderProductByOrderId(this.idtbl_order);
        this.list = data;
    }

    /**
     * 取得訂單基本資料
     *
     * @return JSONObject 取得訂單基本資料
     */
    public JSONObject getOrderData() {
        JSONObject jso = new JSONObject();
        jso.put("idtbl_order", getOrderID());
        jso.put("memberID", getMemberID());
        jso.put("buyer_name", getBuyerName());        
        jso.put("ship_address", getShipAddress());
        jso.put("cellphone", getCellphone());
        jso.put("product_delivery", getProductDelivery());
        jso.put("payment", getPayment());
        jso.put("order_status", getOrderStatus());
        jso.put("total_price", getTotalPrice());
        jso.put("create", getCreateTime());      
        return jso;
    }

    /**
     * 取得訂單產品資料
     *
     * @return JSONArray 取得訂單產品資料
     */
    public JSONArray getOrderProductData() {
        JSONArray result = new JSONArray();

        for(int i=0 ; i < this.list.size() ; i++) {
            result.put(this.list.get(i).getData());
        }

        return result;
    }

    /**
     * 取得訂單所有資訊
     *
     * @return JSONObject 取得訂單所有資訊
     */
    public JSONObject getOrderAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("order_info", getOrderData());
        jso.put("product_info", getOrderProductData());

        return jso;
    }

    /**
     * 設定訂單產品編號
     */
    public void setOrderProductId(JSONArray data) {
        for(int i=0 ; i < this.list.size() ; i++) {
            this.list.get(i).setId((int) data.getLong(i));
        }
    }

}

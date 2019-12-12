package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Order {

    /** id，訂單編號 */
    private int id;

    /** first_name，會員姓名 */
    private String first_name;

    /** last_name，會員姓 */
    private String last_name;

    /** email，會員電子郵件信箱 */
    private String email;

    /** address，會員地址 */
    private String address;

    /** phone，會員手機 */
    private String phone;

    /** list，訂單列表 */
    private ArrayList<OrderItem> list = new ArrayList<OrderItem>();

    /** create，訂單創建時間 */
    private Timestamp create;

    /** modify，訂單修改時間 */
    private Timestamp modify;

    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private OrderItemHelper oph = OrderItemHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     */
    public Order(String first_name, String last_name, String email, String address, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.create = Timestamp.valueOf(LocalDateTime.now());
        this.modify = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     * @param create 訂單創建時間
     * @param modify 訂單修改時間
     */
    public Order(int id, String first_name, String last_name, String email, String address, String phone, Timestamp create, Timestamp modify) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.create = create;
        this.modify = modify;
        getOrderProductFromDB();
    }

    /**
     * 新增一個訂單產品及其數量
     */
    public void addOrderProduct(Product pd, int quantity) {
        this.list.add(new OrderItem(pd, quantity));
    }

    /**
     * 新增一個訂單產品
     */
    public void addOrderProduct(OrderItem op) {
        this.list.add(op);
    }

    /**
     * 設定訂單編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getId() {
        return this.id;
    }

    /**
     * 取得訂單會員的名
     *
     * @return String 回傳訂單會員的名
     */
    public String getFirstName() {
        return this.first_name;
    }

    /**
     * 取得訂單會員的姓
     *
     * @return String 回傳訂單會員的姓
     */
    public String getLastName() {
        return this.last_name;
    }

    /**
     * 取得訂單信箱
     *
     * @return String 回傳訂單信箱
     */
    public String getEmail() {
        return this.email;
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
     * 取得訂單修改時間
     *
     * @return Timestamp 回傳訂單修改時間
     */
    public Timestamp getModifyTime() {
        return this.modify;
    }

    /**
     * 取得訂單地址
     *
     * @return String 回傳訂單地址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 取得訂單電話
     *
     * @return String 回傳訂單電話
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public ArrayList<OrderItem> getOrderProduct() {
        return this.list;
    }

    /**
     * 從 DB 中取得訂單產品
     */
    private void getOrderProductFromDB() {
        ArrayList<OrderItem> data = oph.getOrderProductByOrderId(this.id);
        this.list = data;
    }

    /**
     * 取得訂單基本資料
     *
     * @return JSONObject 取得訂單基本資料
     */
    public JSONObject getOrderData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("first_name", getFirstName());
        jso.put("last_name", getLastName());
        jso.put("email", getEmail());
        jso.put("address", getAddress());
        jso.put("phone", getPhone());
        jso.put("create", getCreateTime());
        jso.put("modify", getModifyTime());

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

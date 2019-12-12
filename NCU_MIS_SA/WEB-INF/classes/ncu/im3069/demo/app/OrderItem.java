package ncu.im3069.demo.app;

import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;

public class OrderItem {

    /** id，產品細項編號 */
    private int id;

    /** pd，產品 */
    private Product pd;

    /** quantity，產品數量 */
    private int quantity;

    /** price，產品價格 */
    private double price;

    /** subtotal，產品小計 */
    private double subtotal;

    /** ph，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單細項時
     *
     * @param pd 會員電子信箱
     * @param quantity 會員密碼
     */
    public OrderItem(Product pd, int quantity) {
        this.pd = pd;
        this.quantity = quantity;
        this.price = this.pd.getPrice();
        this.subtotal = Arith.mul((double) this.quantity, this.price);
    }

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
     *
     * @param order_product_id 訂單產品編號
     * @param order_id 會員密碼
     * @param product_id 產品編號
     * @param price 產品價格
     * @param quantity 產品數量
     * @param subtotal 小計
     */
    public OrderItem(int order_product_id, int order_id, int product_id, double price, int quantity, double subtotal) {
        this.id = order_product_id;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
        getProductFromDB(product_id);
    }

    /**
     * 從 DB 中取得產品
     */
    private void getProductFromDB(int product_id) {
        String id = String.valueOf(product_id);
        this.pd = ph.getById(id);
    }

    /**
     * 取得產品
     *
     * @return Product 回傳產品
     */
    public Product getProduct() {
        return this.pd;
    }

    /**
     * 設定訂單細項編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得訂單細項編號
     *
     * @return int 回傳訂單細項編號
     */
    public int getId() {
        return this.id;
    }

    /**
     * 取得產品價格
     *
     * @return double 回傳產品價格
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * 取得產品細項小計
     *
     * @return double 回傳產品細項小計
     */
    public double getSubTotal() {
        return this.subtotal;
    }

    /**
     * 取得產品數量
     *
     * @return int 回傳產品數量
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * 取得產品細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("id", getId());
        data.put("product", getProduct().getData());
        data.put("price", getPrice());
        data.put("quantity", getQuantity());
        data.put("subtotal", getSubTotal());

        return data;
    }
}

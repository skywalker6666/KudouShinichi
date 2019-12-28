package ncu.im3069.demo.app;

import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;

public class OrderProduct {

    /** id，產品細項編號 */
    private int idtbl_ordeproduct;

    /** pd，產品 */ 
    private Product pd;

    /** product_quantities，產品數量 */
    private int product_quantities;

    /** price，產品價格 */
    private double price;

    /** subtotal，產品小計 */
    private double subtotal;

    /** ph，ProductHelper 之物件與 OrderProduct 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）OrderProduct 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單細項時
     *
     * @param pd 產品
     * @param product_quantities 產品數量
     */
    public OrderProduct(Product pd, int product_quantities) {
        this.pd = pd;
        this.product_quantities = product_quantities;
        this.price = this.pd.getPrice();
        this.subtotal = Arith.mul((double) this.product_quantities, this.price);
    }

    /**
     * 實例化（Instantiates）一個新的（new）OrderProduct 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
     *
     * @param order_product_id 訂單產品編號
     * @param order_id 訂單id
     * @param product_id 產品編號
     * @param price 產品價格
     * @param product_quantities 產品數量
     * @param subtotal 小計
     */
    public OrderProduct(int idtbl_ordeproduct, int order_id, int product_id, double price, int product_quantities, double subtotal) {
        this.idtbl_ordeproduct = idtbl_ordeproduct;
        this.product_quantities = product_quantities;
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
    public void setId(int idtbl_ordeproduct) {
        this.idtbl_ordeproduct = idtbl_ordeproduct;
    }

    /**
     * 取得訂單細項編號
     *
     * @return int 回傳訂單細項編號
     */
    public int getId() {
        return this.idtbl_ordeproduct;
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
        return this.product_quantities;
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
        data.put("product_quantities", getQuantity());
        data.put("subtotal", getSubTotal());

        return data;
    }
}

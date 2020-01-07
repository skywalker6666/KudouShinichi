package ncu.im3069.demo.app;

import java.sql.Timestamp;

import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;

public class OrderProduct {

    /** id，產品細項編號 */
    private int idtbl_ordeproduct;
    private int order_id;

    /** pd，產品 */ 
    private Product pd;
    private Order od;

    /** product_quantities，產品數量 */
    private int product_quantities;

    /** price，產品價格 */
    private int price;

    /** subtotal，產品小計 */
    private double subtotal;
    private String buyer_name;
    private String address;
    private int memberID;

    /** ph，ProductHelper 之物件與 OrderProduct 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();
	private String payment;
	private String product_delivery;
	private Timestamp create;
	private int order_status;
	private String cellphone;
	private int productid;
	private String product_name;
	private int sellerid;

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
    //賣家訂單追蹤用到的
    public OrderProduct(int idtbl_ordeproduct, int order_id, int product_id,String product_name, int seller_id, int price, int product_quantities,String buyer_name,String address, double subtotal,String payment,String product_delivery,Timestamp create,int memberID,int order_status,String cellphone) {

    	this.idtbl_ordeproduct = idtbl_ordeproduct;
        this.order_id=order_id;
        getProductFromDB(product_id);
        getSellerFromDB(seller_id);
        this.price = price;
        this.product_quantities = product_quantities;
        this.buyer_name=buyer_name;
        this.address=address;
        this.subtotal = subtotal;
        this.payment=payment;
        this.product_delivery=product_delivery;
        this.create=create;
        this.memberID=memberID;
        this.cellphone=cellphone;
        this.order_status=order_status;
        this.product_name=product_name;
    }
  //買家訂單追蹤用到的
    public OrderProduct(int idtbl_ordeproduct, int order_id, int product_id, int seller_id, int price, int product_quantities, double subtotal) {
        
    	this.idtbl_ordeproduct = idtbl_ordeproduct;
        
        this.order_id=order_id;
        getProductFromDB(product_id);
        getSellerFromDB(seller_id);
        this.price = price;
        this.product_quantities = product_quantities;
        this.subtotal = subtotal;
      
    }
  public OrderProduct( int order_id, int product_id, int seller_id, int price, int product_quantities, double subtotal) {
        
  
        
        this.order_id=order_id;
        getProductFromDB(product_id);
        getSellerFromDB(seller_id);
        this.price = price;
        this.product_quantities = product_quantities;
        this.subtotal = subtotal;
      
    }

    /**
     * 從 DB 中取得產品
     */
    private void getSellerFromDB(int seller_id) {
        String id = String.valueOf(seller_id);
        this.pd = ph.getBySellerId(id);
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
    public Order getOrder() {
        return this.od;
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
    public int getorderId() {
        return this.order_id;
    }

    public int getPrice() {
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
    public String getPayment() {
        return this.payment;
    }
    public String getProductDelivery() {
        return this.product_delivery;
    }
    /**
     * 取得產品細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getSellerNeedData() {
        JSONObject data = new JSONObject();
        data.put("idtbl_orderproduct", getId());
        data.put("orderID", getorderId());
        data.put("sellerID", getsellerID()); 
        data.put("memberID", getmemberID());
        data.put("productID", getproductID());         
        data.put("product_name", getProductName());        
        data.put("price", getPrice());
        data.put("product_quantities", getQuantity());
        data.put("buyer_name", getbuyerName());
        data.put("ship_address", getAddress());        
        data.put("subtotal", getSubTotal());
        data.put("payment", getPayment());
        data.put("product_delivery", getProductDelivery());
        data.put("create", getCreateTime());
        data.put("order_status", getOrderStatus());
        data.put("cellphone", getCellphone());
        
        

        return data;
    }/**
     * 取得產品細項資料
    *
    * @return JSONObject 回傳產品細項資料
    */
   public JSONObject getData() {
       JSONObject data = new JSONObject();
       data.put("idtbl_orderproduct", getId());
       data.put("productID", getProduct().getData());
       data.put("sellerID", getProduct().getData());
       data.put("price", getPrice());
       data.put("product_quantities", getQuantity());
       data.put("subtotal", getSubTotal());

       return data;
   }
	public String getAddress() {
		return this.address;
	}
	
	public int getmemberID() {
		return this.memberID;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getbuyerName() {
		return this.buyer_name;
	}

	public void setbuyerName(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public Timestamp getCreateTime() {
        return this.create;
    }
	public String getCellphone() {
        return this.cellphone;
    }
	public int getOrderStatus() {
        return this.order_status;
    }
	public String getProductName() {
        return this.product_name;
    }
	public int getproductID() {
		return this.productid;
	}
	public int getsellerID() {
		return this.sellerid;
	}
}
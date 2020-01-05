package ncu.im3069.demo.app;

import org.json.*;

public class Product {

    /** idtbl_product，產品編號 */
    private int idtbl_product;

    /** idtbl_product，會員編號 */
    private String product_name;

    /** price，產品價錢 */
    private int price;
    
    /** inventory，產品庫存 */
    private int inventory;
    
    /** sellerID，賣場編號 */
    private int sellerID;

    /** is_deleted，是否被刪除 */
    private int is_deleted;
    
    /**image，產品圖片*/
    private String image;

    /** product_info，產品資訊 */
	private String product_info;
	
	/** product_info，產品類別 */
	private String type;
	
	/** product_info，產品種類 */
	private String category;
	
	private String created;
	private int total;
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param idtbl_product 產品編號
     */
	public Product(int idtbl_product) {
		this.idtbl_product = idtbl_product;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param product_name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	public Product (int idtbl_product, String product_name,int price,int is_Deleted,String image, String product_info,int Total){
		this.product_name = product_name;
		this.price = price;
		this.idtbl_product=idtbl_product;
		this.is_deleted=is_Deleted;
		this.total=Total;
		this.image = image;
		this.product_info=product_info;

	}
	public Product(String product_name, int price, int inventory, int sellerID,  String image, String product_info, String type, String category) {
		this.product_name = product_name;
		this.price = price;
		this.inventory=inventory;
		this.sellerID=sellerID;		
		this.image = image;
		this.product_info=product_info;
		this.type=type;
		this.category=category;
	//	this.created=created;
	}
	public Product(int idtbl_product,String product_name, int price,int inventory,int sellerID,int is_deleted,String image,String product_info,String type,String category,String created)
	{
		this.idtbl_product = idtbl_product;
		this.product_name = product_name;
		this.price = price;
		this.inventory=inventory;
		this.sellerID=sellerID;		
		this.is_deleted=is_deleted;
		this.image = image;
		this.product_info=product_info;
		this.type=type;
		this.category=category;
		this.created=created;
	}
	public Product(int idtbl_product, String product_name, int price, int inventory, int sellerID, int is_deleted, String image,String product_info,String type,String category) {
		this.idtbl_product = idtbl_product;
		this.product_name = product_name;
		this.price = price;
		this.inventory=inventory;
		this.sellerID=sellerID;		
		this.is_deleted=is_deleted;
		this.image = image;
		this.product_info=product_info;
		this.type=type;
		this.category=category;
	}
	

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param idtbl_product 產品編號
     * @param product_name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param product_info 產品敘述
     */
	public Product(int idtbl_product, String product_name, int price, int inventory, int sellerID, int is_deleted, String image, String product_info) {
		this.idtbl_product = idtbl_product;
		this.product_name = product_name;
		this.price = price;
		this.inventory=inventory;
		this.sellerID=sellerID;
		this.is_deleted=is_deleted;
		this.image = image;
		this.product_info=product_info;
	}
	
	public Product(int idtbl_product, String product_name, int price, int inventory, int sellerID, String image, String product_info, String type, String category) {
		this.idtbl_product = idtbl_product;
		this.product_name = product_name;
		this.price = price;
		this.inventory=inventory;
		this.sellerID=sellerID;
		this.image = image;
		this.product_info=product_info;
		this.type=type;
		this.category=category;
	}
	
    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getID() {
		return this.idtbl_product;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public String getName() {
		return this.product_name;
	}

    /**
     * 取得產品價格
     *
     * @return double 回傳產品價格
     */
	public int getPrice() {
		return this.price;
	}

	/**
     * 取得產品庫存
     *
     * @return int 回傳產品庫存
     */
	public int getInventory() {
		return this.inventory;
	}
	
	/**
     * 取得賣場ID
     *
     * @return int 回傳賣場ID
     */
	public int getSellerID() {
		return this.sellerID;
	}
	
	/**
     * 取得產品狀態
     *
     * @return int 回傳產品是否被刪除
     */
	public int getIsDeleted() {
		return this.is_deleted;
	}	
	
    /**
     * 取得產品圖片
     *
     * @return String 回傳產品圖片
     */
	public String getImage() {
		return this.image;
	}

    /**
     * 取得產品敘述
     *
     * @return String 回傳產品敘述
     */
	public String getProductInfo() {
		return this.product_info;
	}
	
	/**
     * 取得產品類型
     *
     * @return String 回傳產品敘述
     */
	public String getType() {
		return this.type;
	}
	
	/**
     * 取得產品種類
     *
     * @return String 回傳產品敘述
     */
	public String getCategory() {
		return this.category;
	}
	public String getCreated() {
		return this.created;
	}

    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("idtbl_product", getID());
        jso.put("product_name", getName());
        jso.put("price", getPrice());
        jso.put("inventory", getInventory());
        jso.put("sellerID", getSellerID());
        jso.put("is_deleted", getIsDeleted());
        jso.put("image", getImage());
        jso.put("product_info", getProductInfo());
        jso.put("type", getType());
        jso.put("category", getCategory());
        jso.put("created", getCreated());

        return jso;
    }
}

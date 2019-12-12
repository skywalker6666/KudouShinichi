package ncu.im3069.demo.app;

import org.json.*;

public class Product {

    /** id，會員編號 */
    private int id;

    /** id，會員編號 */
    private String name;

    /** id，會員編號 */
    private double price;

    /** id，會員編號 */
    private String image;

    /** id，會員編號 */
	private String describe;

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Product(int id) {
		this.id = id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	public Product(String name, double price, String image) {
		this.name = name;
		this.price = price;
		this.image = image;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
	public Product(int id, String name, double price, String image, String describe) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.describe = describe;
	}

    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getID() {
		return this.id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public String getName() {
		return this.name;
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
	public String getDescribe() {
		return this.describe;
	}

    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("price", getPrice());
        jso.put("image", getImage());
        jso.put("describe", getDescribe());

        return jso;
    }
}

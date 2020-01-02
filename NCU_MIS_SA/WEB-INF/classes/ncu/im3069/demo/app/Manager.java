package ncu.im3069.demo.app;

import org.json.*;
import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class Manager
 * Manager類別（class）具有會員所需要之屬性與方法，並且儲存與會員相關之商業判斷邏輯<br>
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class Manager {
    
    /** id，管理員編號 */
    private int managerId;
    
    /** email，管理員名稱 */
    private String managerName;
    
    /** name，管理員階層 */
    private int isLeader;
    
    /** password，會員密碼 */
    private String managerPassword;
    
    private int isDeleted;
    
    /** headSticker，會員頭貼 */
  //  private String headSticker;
    
    /** birthday，會員生日 */
  //  private String birthday;
    
    /** isSeller，會員是否為賣家 */
  //  private int isSeller;
    
    /** managerAccountName，管理員帳戶名稱 */
   // private String managerAccountName;
    

    
    /** mh，ManagerHelper之物件與Manager相關之資料庫方法（Sigleton） */
    private ManagerHelper mgh =  ManagerHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Manager物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立會員資料時，產生一名新的會員
     *
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     */
    public Manager(String managerName, String password)
    {
    	this.managerName=managerName;
    	this.managerPassword=password;
    //	update();
    }
    public Manager(String managerName,String password,int isLeader,int isDeleted) {
       // this.email = email;
        this.managerPassword = password;
        this.managerName = managerName;
        this.isLeader= isLeader;
        this.isDeleted= isDeleted;
     //   this.headSticker=headSticker;
      //  this.birthday=birthday;
       // this.isSeller=isSeller;
        update();
    }
    public Manager(int managerid,String managerName,String password) {
         this.managerId = managerid;
         this.managerPassword = password;
         this.managerName = managerName;

      //   this.headSticker=headSticker;
       //  this.birthday=birthday;
        // this.isSeller=isSeller;
         update();
     }
  

    /**
     * 實例化（Instantiates）一個新的（new）Manager物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新會員資料時，產生一名會員同時需要去資料庫檢索原有更新時間分鐘數與會員組別
     * 
     * @param id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     */
   public Manager(int managerId,String managerName, String password,int isLeader, int isDeleted) {
        this.managerId = managerId;
//        this.email = email;
        this.managerPassword = password;
        this.managerName = managerName;
 //       this.headSticker=headSticker;
  //      this.birthday=birthday;//
        this.isLeader=isLeader;//
        this.isDeleted=isDeleted;
        /** 取回原有資料庫內該名會員之更新時間分鐘數與組別 */
  //      getLoginTimesStatus();
        /** 計算會員之組別 */
    //    calcAccName();
    }      
    
    /**
     * 實例化（Instantiates）一個新的（new）Manager物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢會員資料時，將每一筆資料新增為一個會員物件
     *
     * @param id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     * @param login_times 更新時間的分鐘數
     * @param status the 會員之組別
     */
//    public Manager(int id, String email, String password, String name, String headSticker, String birthday, int isSeller, int login_times, String status) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.headSticker=headSticker;
//        this.birthday=birthday;//
//        this.isSeller=isSeller;//
//        this.login_times = login_times;
//        this.status = status;
//    }
    
    /**
     * 取得會員之編號
     *
     * @return the id 回傳會員編號
     */
    public int getID() {
        return this.managerId;
    }

    /**
     * 取得會員之電子郵件信箱
     *
     * @return the email 回傳會員電子郵件信箱
     */

    
    /**
     * 取得會員之姓名
     *
     * @return the name 回傳會員姓名
     */
    public String getmanagerName() {
        return this.managerName;
    }

    /**
     * 取得會員之頭貼
     *
     * @return the headSticker 回傳會員頭貼
     */
    
    /**
     * 取得會員之生日
     *
     * @return the headSticker 回傳會員頭貼
     */

    /**
     * 取得會員之頭貼
     *
     * @return the headSticker 回傳會員頭貼
     */
    public int getIsLeader() {
        return this.isLeader;
    }
    /**
     * 取得會員之密碼
     *
     * @return the password 回傳會員密碼
     */
    public String getPassword() {
        return this.managerPassword;
    }
    
    public int getisDeleted() {
     return this.isDeleted;
    }
    
    /**
     * 取得更新資料時間之分鐘數
     *
     * @return the login times 回傳更新資料時間之分鐘數
     */

    
    /**
     * 取得會員資料之會員組別
     *
     * @return the status 回傳會員組別
     */

    
    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        /** 取得更新資料時間（即現在之時間）之分鐘數 */
        Calendar calendar = Calendar.getInstance();
   //     this.login_times = calendar.get(Calendar.MINUTE);
        /** 計算帳戶所屬之組別 */
     //   calcAccName();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.managerId != 0) {
            /** 若有則將目前更新後之資料更新至資料庫中 */
     //       mh.updateLoginTimes(this);
            /** 透過ManagerHelper物件，更新目前之會員資料置資料庫中 */
            data = mgh.update(this);
        }
        
        return data;
    }
    
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("idtbl_manager", getID());

        jso.put("name", getmanagerName());
        jso.put("password", getPassword());
        jso.put("isLeader", getIsLeader());
        jso.put("isDeleted", getisDeleted());
        
        return jso;
    }
    
    /**
     * 取得資料庫內之更新資料時間分鐘數與會員組別
     *
     */
//    private void getLoginTimesStatus() {
        /** 透過ManagerHelper物件，取得儲存於資料庫的更新時間分鐘數與會員組別 */
      //  JSONObject data = mh.getLoginTimesStatus(this);
        /** 將資料庫所儲存該名會員之相關資料指派至Manager物件之屬性 */
      //  this.login_times = data.getInt("login_times");
       // this.status = data.getString("status");
//    }
    
    /**
     * 計算會員之組別<br>
     * 若為偶數則為「偶數會員」，若為奇數則為「奇數會員」
     */

}
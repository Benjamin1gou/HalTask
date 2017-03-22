package local.hal.st21.android.saigoku3340024;

/**
 * Created by ohs40024 on 2016/01/29.
 * 寺情報を格納するエンティティクラス
 */
public class Temple {
    /**
     * 主キー
     */
    private int _id;
    /**
     * 寺名
     */
    private String _name;
    /**
     * 本尊名
     */
    private String _honzon;
    /**
     * 宗旨
     */
    private String _shushi;
    /**
     * 所在地
     */
    private String _address;
    /**
     * URL
     */
    private String _url;
    /**
     * 感想
     */
    private String _note;

    /*******セッター*********************/
    public void setId(int id){
        _id = id;
//        System.out.println("_id"+_id);
    }
    public void setName(String name){
        _name = name;
//        System.out.println("_name"+_name);
    }
    public void setHonzon(String honzon){
        _honzon = honzon;
//        System.out.println("_honzon"+_honzon);
    }
    public void setShushi(String shushi){
        _shushi = shushi;
//        System.out.println("_shushi"+_shushi);
    }
    public void setAddress(String address){
        _address = address;
//        System.out.println("_address"+_address);
    }
    public void setUrl(String url){
        _url = url;
//        System.out.println("_url"+ _url);
    }
    public void setNote(String note){
        _note = note;
//        System.out.println("_note"+_note);
    }
    /******ゲッター************************/
    public int getId(){
        return _id;
    }
    public String getName(){
        return _name;
    }
    public String getHonzon(){
        return _honzon;
    }
    public String getShushi(){
        return _shushi;
    }
    public String getAddress(){
        return _address;
    }
    public String getUrl(){
        return _url;
    }
    public String getNote(){
        return _note;
    }
}

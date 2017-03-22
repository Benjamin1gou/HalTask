package local.hal.st21.android.favoriteshops40024;

/**
 * Created by ohs40024 on 2016/02/11.
 */
public class Shop {

    /**
     * 主キー値
     */
    private int _id;

    /**
     * 店名
     */
    private String _name;

    /**
     * 電話番号
     */
    private String _tel;

    /**
     * URL
     */
    private String _url;

    /**
     * メモ
     */
    private String _note;


    /**
     *セッター
     */
    public void setId(int id) {
        this._id = id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setTel(String tel) {
        this._tel = tel;
    }

    public void setUrl(String url) {
        this._url = url;
    }

    public void setNote(String note) {
        this._note = note;
    }

    /**
     * ゲッター
     */
    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getTel() {
        return _tel;
    }

    public String getUrl() {
        return _url;
    }

    public String getNote() {
        return _note;
    }
}

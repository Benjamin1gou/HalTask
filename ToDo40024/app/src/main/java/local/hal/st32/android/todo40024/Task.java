package local.hal.st32.android.todo40024;


import java.util.Date;

/**
 * Created by Tester on 16/04/13.
 */
public class Task {
    /**
     * 主キーのフィールド
     */
    private int _id;

    /**
     * タスク名のフィールド
     */
    private String _name;

    /**
     * 期限のフィールド
     */
    private String _deadline;

    /**
     * 完了状態のフィールド
     */
    private int _done;

    /**
     * 詳細のフィールド
     */
    private String _note;

    /**
     * セッターたち
     */
    public void setId(int _id) {
        this._id = _id;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setDeadline(String _deadline) {
        this._deadline = _deadline;
    }

    public void setDone(int _done) {
        this._done = _done;
    }

    public void setNote(String _note) {
        this._note = _note;
    }

    /**
     * ゲッターたち
     */
    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getDeadline() {
        return _deadline;
    }

    public int getDone() {
        return _done;
    }

    public String getNote() {
        return _note;
    }
}

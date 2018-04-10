package scratch;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity {
    private long row_id;
    private long budget_unit_id;
    private String name;
    private int type;
    private String dateCreated;
    private String dateModified;
    private BigDecimal spentMoney;
    private BigDecimal addedMoney;
    private String note;
    private long photoid;
    private static final int INCOME_TYPE = 1;
    private static final int EXPENSES_TYPE = 2;

    public Activity(){

    }

    public Activity(long row_id, long budget_unit_id, String name, int type, BigDecimal spentMoney, BigDecimal addedMoney, String note, long photoid){
        this(budget_unit_id, name, type, spentMoney, addedMoney, note, photoid);
        setRow_id(row_id);

    }

    public Activity(long budget_unit_id, String name, int type, BigDecimal spentMoney, BigDecimal addedMoney, String note, long photoid){
        setBudget_unit_id(budget_unit_id);
        setName(name);
        setType(type);
        setSpentMoney(spentMoney);
        setAddedMoney(addedMoney);
        setNote(note);
        setPhotoid(photoid);
    }



    public long getRow_id() {
        return row_id;
    }

    public void setRow_id(long row_id) {
        this.row_id = row_id;
    }

    public long getBudget_unit_id() {
        return budget_unit_id;
    }

    public void setBudget_unit_id(long budget_unit_id) {
        this.budget_unit_id = budget_unit_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    //this is for automatically setting the date when user creates a Budget Unit
    public void setDateCreated() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        this.dateCreated = dateFormat.format(date);
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    //this is for automatically setting the date when user modifies a Budget Unit
    public void setDateModified() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        this.dateModified = dateFormat.format(date);
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public BigDecimal getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(BigDecimal spentMoney) {
        if(type==EXPENSES_TYPE){
            this.spentMoney = spentMoney;
        }
    }

    public BigDecimal getAddedMoney() {
        return addedMoney;
    }

    public void setAddedMoney(BigDecimal addedMoney) {
        if(type==INCOME_TYPE){
            this.addedMoney = addedMoney;
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getPhotoid() {
        return photoid;
    }

    public void setPhotoid(long photoid) {
        this.photoid = photoid;
    }
}

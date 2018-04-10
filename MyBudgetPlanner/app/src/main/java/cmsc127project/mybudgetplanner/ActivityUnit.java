package cmsc127project.mybudgetplanner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityUnit implements Serializable{
    private long row_id;
    private long budget_row_id;
    private String name;
    private int type;
    private String dateCreated;
    private String dateModified;
    private BigDecimal addedMoney;
    private BigDecimal spentMoney;
    private String note;
    private long photo_id;

    protected static final int MONEY_ADDED = 1;
    protected static final int MONEY_SPENT = 2;



    public ActivityUnit(){

    }

    public ActivityUnit(long row_id, long budget_row_id, String name, int type, BigDecimal spentMoney, BigDecimal addedMoney, String note, long photoid) {
        this(budget_row_id, name, type, spentMoney, addedMoney, note, photoid);
        setRow_id(row_id);
    }

    public ActivityUnit(long budget_row_id, String name, int type, BigDecimal spentMoney, BigDecimal addedMoney, String note, long photoid){
        setBudget_row_id(budget_row_id);
        setName(name);
        setType(type);
        setDateCreated();
        setDateModified("");
        setNote(note);
        setPhoto_id(photoid);

        if(type==MONEY_ADDED){
            setAddedMoney(addedMoney);
            setSpentMoney(new BigDecimal(0.0));
        }else{
            if(type==MONEY_SPENT){
                setSpentMoney(spentMoney);
                setAddedMoney(new BigDecimal(0.0));
            }
        }
    }

    public long getBudget_row_id() {
        return budget_row_id;
    }

    public void setBudget_row_id(long budget_row_id) {
        this.budget_row_id = budget_row_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(long photo_id) {
        this.photo_id = photo_id;
    }

    public long getRow_id(){
        return row_id;
    }

    public void setRow_id(long row_id){
        this.row_id = row_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    //this is for automatically setting the date when user creates a Budget Unit
    public void setDateCreated() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyy 'at' hh:mm aaa", Locale.getDefault());
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
                "dd/MM/yyy 'at' hh:mm aaa", Locale.getDefault());
        Date date = new Date();
        this.dateModified = dateFormat.format(date);
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }


    public BigDecimal getAddedMoney() {
        return addedMoney;
    }

    public void setAddedMoney(BigDecimal addedMoney) {
        this.addedMoney = addedMoney;
    }

    public BigDecimal getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(BigDecimal spentMoney) {
        this.spentMoney = spentMoney;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getText(){
        if(this.getName().length() >= 30){
            return this.getName().substring(0, 20) + "...";
        }else{
            return this.getName();
        }
    }

    public String getDateLabel(){
        if(getDateModified().equals("")){
            return getDateCreated();
        }else{
            return getDateModified();
        }
    }

    public String getMoney(){
        if(type==MONEY_ADDED){
            return getAddedMoney().toString();
        }else{
            if(type==MONEY_SPENT){
                return getSpentMoney().toString();
            }
        }

        return "WRONG TYPE";
    }
}

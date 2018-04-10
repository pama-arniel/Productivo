package cmsc127project.mybudgetplanner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Budget implements Serializable{
    private long row_id;
    private String name;
    private String dateCreated;
    private String dateModified;
    private String duration;
    private BigDecimal currentMoney;
    private BigDecimal origMoney;
    private BigDecimal addedMoney;
    private BigDecimal spentMoney;
    private String note;

    public Budget(){

    }

    public Budget(long row_id, String name, String duration, BigDecimal origMoney, String note) {
        this(name, duration, origMoney, note);
        setRow_id(row_id);
    }

    public Budget(String name, String duration, BigDecimal origMoney, String note){
        setName(name);
        setDuration(duration);
        setOrigMoney(origMoney);
        setNote(note);
        setDateCreated();
        setDateModified("");
        setCurrentMoney(origMoney);
        setAddedMoney(new BigDecimal(0.0));
        setSpentMoney(new BigDecimal(0.0));
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public BigDecimal getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(BigDecimal currentMoney) {
        this.currentMoney = currentMoney;
    }

    public BigDecimal getOrigMoney() {
        return origMoney;
    }

    public void setOrigMoney(BigDecimal origMoney) {
        this.origMoney = origMoney;
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

    public String getPercent(){
        BigDecimal percentLeftDecimal = this.getCurrentMoney().divide(this.getOrigMoney()).multiply(new BigDecimal(100));
        return percentLeftDecimal.toString();
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
}

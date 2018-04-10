package cmsc127project.mybudgetplanner;

import android.app.Activity;
import java.math.BigDecimal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewBudget extends AppCompatActivity {
    EditText addBudgetName, addbudgetOrigMoney, addBudgetDescription;
    Button okBudgetButton;
    //private int year, month, day;
    DBHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbudget);
        myDBHelper = new DBHelper(this.getApplicationContext());

        addBudgetName = (EditText) findViewById(R.id.addBudgetName);
        addbudgetOrigMoney = (EditText) findViewById(R.id.addbudgetOrigMoney);
        addBudgetDescription = (EditText) findViewById(R.id.addBudgetDescription);
        okBudgetButton = (Button) findViewById(R.id.okBudgetButton);
    }

    @Override
    public void onDestroy() {
        myDBHelper.close();
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void respondToSave(View v){
        Log.v("ANB", "Start");
        String name = addBudgetName.getText().toString();
        String duration = "ILISAN PA NI SUNOD";
        String origMoneyStr = addbudgetOrigMoney.getText().toString();
        String note = addBudgetDescription.getText().toString();
        boolean moneyError = false;
        boolean nameError = false;

        if(TextUtils.isEmpty(name)){
            addBudgetName.setError("This item cannot be empty.");
            nameError = true;
        }

        if(TextUtils.isEmpty(origMoneyStr)){
            addbudgetOrigMoney.setError("This item cannot be empty.");
            moneyError = true;
        }else{
            BigDecimal origMoney = new BigDecimal( origMoneyStr );
            if(origMoney.compareTo(new BigDecimal(0.0)) <= 0){
                addbudgetOrigMoney.setError("Your money must be above 0.");
                moneyError = true;
            }
        }

        if(!nameError && !moneyError){
            BigDecimal origMoney = new BigDecimal( origMoneyStr );
            myDBHelper.createBudget(name, duration, origMoney, note);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    /*@Override
    TODO this is to be done later if makaya sa time
    protected Dialog onCreateDialog(int id) {
        if(id==999){
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }
    }*/
}

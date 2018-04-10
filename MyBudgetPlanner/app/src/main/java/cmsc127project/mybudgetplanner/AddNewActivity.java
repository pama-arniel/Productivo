package cmsc127project.mybudgetplanner;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;

public class AddNewActivity extends AppCompatActivity {
    private EditText addActivityName, addActivityMoney, addActivityNote;
    private Button okActivityButton;
    DBHelper myDBHelper;
    private long photoid = 0;
    long budget_row_id;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);
        myDBHelper = new DBHelper(this.getApplicationContext());

        addActivityName = (EditText) findViewById(R.id.addActivityName);
        addActivityMoney = (EditText) findViewById(R.id.addActivityMoney);
        addActivityNote = (EditText) findViewById(R.id.addActivityNote);
        okActivityButton = (Button) findViewById(R.id.okActivityButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            budget_row_id = (long) bundle.get("BUDGET_ROW_ID");
            type = (int) bundle.get("TYPE");
        }

        if(type==ActivityUnit.MONEY_ADDED){
            addActivityMoney.setHint("Add money");
        }else{
            if(type==ActivityUnit.MONEY_SPENT){
                addActivityMoney.setHint("Spend money");
            }
        }
    }

    @Override
    public void onDestroy() {
        myDBHelper.close();
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void respondToActivitySave(View v){
        String name = addActivityName.getText().toString();
        String moneyInvolved = addActivityMoney.getText().toString();


        String note = addActivityNote.getText().toString();
        boolean moneyError = false;
        boolean nameError = false;

        if(TextUtils.isEmpty(name)){
            addActivityName.setError("This item cannot be empty.");
            nameError = true;
        }

        if(TextUtils.isEmpty(moneyInvolved)){
            addActivityMoney.setError("This item cannot be empty.");
            moneyError = true;
        }else{
            BigDecimal origMoney = new BigDecimal( moneyInvolved );
            if(origMoney.compareTo(new BigDecimal(0.0)) <= 0){
                addActivityMoney.setError("Your money must be above 0.");
                moneyError = true;
            }
        }

        if(!nameError && !moneyError && type!=0){
            BigDecimal money = new BigDecimal( moneyInvolved );
            myDBHelper.createActivity(budget_row_id, name, type, money, money, note, photoid++);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

}

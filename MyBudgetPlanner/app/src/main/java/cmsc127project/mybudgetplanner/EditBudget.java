package cmsc127project.mybudgetplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;

public class EditBudget extends AppCompatActivity {
    private TextView displayBudgetCurrentMoney, displayBudgetOrigMoney, displayBudgetMoneySpent,
            displayBudgetAddedMoney, displayBudgetDateCreated;
    private EditText editBudgetName, editBudgetDescription;
    private Button displayBudgetUpdate, displayBudgetAddActivity, displayBudgetSpendMoney,displayBudgetSearchActivity;
    private ListView listViewInBudget;

    private static final int ADDACTIVITY_ACTIVITY = 3;
    private static final int DISPLAYACTIVITY_ACTIVITY = 4;
    DBHelper myDBHelper;
    Budget budget;
    int activity_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_budget_item);
        myDBHelper = new DBHelper(this.getApplicationContext());

        editBudgetName = (EditText) findViewById(R.id.displayBudgetName);
        displayBudgetCurrentMoney = (TextView) findViewById(R.id.displayBudgetCurrentMoney);
        displayBudgetOrigMoney = (TextView) findViewById(R.id.displayBudgetOrigMoney);
        displayBudgetMoneySpent = (TextView) findViewById(R.id.displayBudgetMoneySpent);
        displayBudgetAddedMoney = (TextView) findViewById(R.id.displayBudgetAddedMoney);
        displayBudgetDateCreated = (TextView) findViewById(R.id.displayBudgetDateCreated);
        editBudgetDescription = (EditText) findViewById(R.id.editBudgetDescription);

        displayBudgetUpdate = (Button) findViewById(R.id.displayBudgetUpdate);
        displayBudgetAddActivity = (Button) findViewById(R.id.displayBudgetAddMoneyActivity);
        displayBudgetSpendMoney = (Button) findViewById(R.id.displayBudgetSpendMoney);
        displayBudgetSearchActivity = (Button) findViewById(R.id.displayBudgetSearchActivity);

        listViewInBudget = (ListView) findViewById(R.id.displayBudgetListView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            budget = (Budget) bundle.get("BUDGET");
            if(budget != null){
                editBudgetName.setText(budget.getName());
                displayBudgetOrigMoney.setText(
                        displayBudgetOrigMoney.getText() +
                        "Php " + budget.getOrigMoney().toString());

                displayBudgetDateCreated.setText(budget.getDateLabel());
                editBudgetDescription.setText(budget.getNote());
            }
        }

        populateBudgetListViewFromDB();
        registerListClickCallback();
    }

    @Override
    protected void onDestroy() {
        myDBHelper.close();
        super.onDestroy();
        getDelegate().onDestroy();
    }

    /*
	 * UI Button Callbacks
	 */
    protected void populateBudgetListViewFromDB(){
        List<ActivityUnit> allActivitiesUnderThisBudget = myDBHelper.getAllActivitiesInBudgetUnitID(budget.getRow_id());

        if (listViewInBudget.getAdapter() == null) {
            ActivitiesCustomAdapter listadapter = new ActivitiesCustomAdapter(this.getApplicationContext(), allActivitiesUnderThisBudget);
            listadapter.notifyDataSetChanged();
            listViewInBudget.setAdapter(listadapter);
        } else {
            ((ActivitiesCustomAdapter)listViewInBudget.getAdapter()).updateData(allActivitiesUnderThisBudget);
            listViewInBudget.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        }
    }

    public void setMoneyDisplays(List<ActivityUnit> allActivitiesUnderThisBudget){
        //TODO: Calculate
        //Total added money
        //Total spent money
        //Current money = (total added money + original money) - total spent money
        //TODO: Display them
        BigDecimal addedMoney = new BigDecimal(0.0);
        BigDecimal spentMoney = new BigDecimal(0.0);
        BigDecimal currentMoney = new BigDecimal(0.0);
        int len = allActivitiesUnderThisBudget.size();
        for(int i = 0; i < len; i++){
            ActivityUnit ac = allActivitiesUnderThisBudget.get(i);
            if(ac.getType()==ActivityUnit.MONEY_ADDED){
                addedMoney.add(ac.getAddedMoney());
            }
            else if(ac.getType()==ActivityUnit.MONEY_SPENT){
                spentMoney.add(ac.getSpentMoney());
            }
        }
    }

    private void registerListClickCallback(){
        listViewInBudget.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView activityNameDisplay = (TextView) view.findViewById(R.id.activityNameDisplay);
                        TextView moneyInvolved = (TextView) view.findViewById(R.id.activityMoneyInvolved);
                        Toast.makeText(getApplicationContext(),
                                activityNameDisplay.getText().toString() + "\n" + moneyInvolved.getText().toString()
                                , Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void respondToUpdate(View v){
        String name = editBudgetName.getText().toString();
        if(TextUtils.isEmpty(name)){
            editBudgetName.setError("This item cannot be empty.");
        }else{
            String description = editBudgetDescription.getText().toString();
            Budget updatedBudget = budget;
            updatedBudget.setName(name);
            updatedBudget.setNote(description);
            myDBHelper.updateBudget(updatedBudget);
            Toast.makeText(getApplicationContext(),
                    "Edited budget unit!", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    public void addMoneyActivity(View v){
        activity_type = ActivityUnit.MONEY_ADDED;
        addNewActivity(v);
    }

    public void spendMoneyActivity(View v){
        activity_type = ActivityUnit.MONEY_SPENT;
        addNewActivity(v);
    }

    public void addNewActivity(View v){
        Intent addNewActivity = new Intent(EditBudget.this, AddNewActivity.class);
        addNewActivity.putExtra("BUDGET_ROW_ID", budget.getRow_id());
        addNewActivity.putExtra("TYPE", activity_type);
        startActivityForResult(addNewActivity, ADDACTIVITY_ACTIVITY);
    }

    public void searchActivityInBudget(View v){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDACTIVITY_ACTIVITY && resultCode == Activity.RESULT_OK){
            populateBudgetListViewFromDB();
        }

        if(requestCode == DISPLAYACTIVITY_ACTIVITY && resultCode == Activity.RESULT_OK){
            populateBudgetListViewFromDB();
        }
    }

    public class ActivitiesCustomAdapter extends ArrayAdapter {
        List<ActivityUnit> allActivitiesUnderThisBudget;

        ActivitiesCustomAdapter(Context context, List<ActivityUnit> allActivitiesUnderThisBudget){
            super(context, R.layout.activitylist_custom_layout, allActivitiesUnderThisBudget);
            this.allActivitiesUnderThisBudget = allActivitiesUnderThisBudget;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.activitylist_custom_layout, parent, false);
            final ActivityUnit ac = allActivitiesUnderThisBudget.get(position);

            TextView activityNameDisplay = (TextView) rowView.findViewById(R.id.activityNameDisplay);
            TextView activityTxtDate = (TextView) rowView.findViewById(R.id.activityTxtDate);
            TextView activityMoneyInvolved = (TextView) rowView.findViewById(R.id.activityMoneyInvolved);
            ImageView activityButtonEdit = (ImageView) rowView.findViewById(R.id.activityButtonEdit);
            ImageView activityButtonDelete = (ImageView) rowView.findViewById(R.id.activityButtonDelete);

            if(ac != null){
                activityNameDisplay.setText(ac.getText());
                activityTxtDate.setText(ac.getDateLabel());

                int activityType = ac.getType();
                if(activityType==ActivityUnit.MONEY_ADDED){
                    activityMoneyInvolved.setText("Added: " + ac.getAddedMoney());
                }else{
                    if(activityType==ActivityUnit.MONEY_SPENT){
                        activityMoneyInvolved.setText("Spent: " + ac.getSpentMoney());
                    }
                }

                activityButtonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editActivityUnit(ac);
                    }
                });
                activityButtonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteActivityUnit(ac);
                    }
                });
            }


            return rowView;
        }

        public void updateData(List<ActivityUnit> allActivitiesUnderABudget){
            this.allActivitiesUnderThisBudget.clear();
            this.allActivitiesUnderThisBudget.addAll(allActivitiesUnderABudget);
            notifyDataSetChanged();
        }

        //TODO After activities are displayed
        public void editActivityUnit(ActivityUnit ac){
            Intent displayActivityItem = new Intent(EditBudget.this, EditActivity.class);
            displayActivityItem.putExtra("ACTIVITY", ac);
            startActivityForResult(displayActivityItem, DISPLAYACTIVITY_ACTIVITY);
        }

        public void deleteActivityUnit(ActivityUnit ac){
            myDBHelper.deleteActivityUnit(ac);
            ActivitiesCustomAdapter listadapter = (ActivitiesCustomAdapter) listViewInBudget.getAdapter();
            listadapter.remove(ac);
            listadapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),
                    "Deleted activity unit!"
                    , Toast.LENGTH_SHORT).show();
        }
    }


}

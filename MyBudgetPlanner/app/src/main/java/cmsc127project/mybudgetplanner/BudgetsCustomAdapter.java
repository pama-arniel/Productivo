package cmsc127project.mybudgetplanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;


public class BudgetsCustomAdapter extends ArrayAdapter{

    List<Budget> allBudgets;

    BudgetsCustomAdapter(Context context, List<Budget> allBudgets){
        super(context, R.layout.budgetlist_custom_layout, allBudgets);
        this.allBudgets = allBudgets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater li = LayoutInflater.from(getContext());

        View rowView = inflater.inflate(R.layout.budgetlist_custom_layout, parent, false);
        Budget bu = allBudgets.get(position);


        //View customView = li.inflate(R.layout.custom_budgetlist_item, parent, false);
        //Budget bu = getItem(position);

        TextView budgetName = (TextView) rowView.findViewById(R.id.budgetNameDisplay);
        TextView percent = (TextView) rowView.findViewById(R.id.currentPercent);
        TextView date = (TextView) rowView.findViewById(R.id.budgetTxtDate);

        if(bu!=null){
            if(bu.getName().length() >= 30){
                budgetName.setText(bu.getName().substring(0, 20) + "...");
            }else{
                budgetName.setText(bu.getName());
            }


            if(bu.getDateModified().equals("")){
                date.setText(bu.getDateCreated());
            }else{
                date.setText(bu.getDateModified());
            }

            Log.e("BudgetsCustomAdapter", bu.getPercent());
            percent.setText(percent.getText() + bu.getPercent() + "%");

            //progressBar.setMax(100);
            //progressBar.setProgress(50);
            //progressBar.setProgress(Integer.valueOf(percentLeftStr));
        }


        return rowView;
    }

    public void updateData(List<Budget> allBudgets){
        this.allBudgets.clear();
        this.allBudgets.addAll(allBudgets);
        notifyDataSetChanged();
    }
}

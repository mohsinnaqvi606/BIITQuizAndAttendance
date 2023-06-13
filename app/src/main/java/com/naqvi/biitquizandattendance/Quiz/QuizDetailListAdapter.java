package com.naqvi.biitquizandattendance.Quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.naqvi.biitquizandattendance.R;

import java.util.ArrayList;

class QuizDetailListAdapter extends ArrayAdapter<Quiz> {
    public ArrayList<Quiz> adapterItems;
    Context context;
    public QuizDetailListAdapter(Context context, ArrayList<Quiz> adapterItems) {
        super(context, R.layout.quiz_detail_list_item,adapterItems);
        this.adapterItems = adapterItems;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.quiz_detail_list_item,parent,false);

        TextView tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        TextView tvOption1 = (TextView) view.findViewById(R.id.tvOption1);
        TextView tvOption2 = (TextView) view.findViewById(R.id.tvOption2);
        TextView tvOption3 = (TextView) view.findViewById(R.id.tvOption3);

        Quiz item = adapterItems.get(position);

        tvQuestion.setText((position+1)+") "+item.Question);
        tvOption1.setText("a)"+item.Option1);
        tvOption2.setText("b)"+item.Option2);
        tvOption3.setText("c)"+item.Option3);

        return view;
    }
}
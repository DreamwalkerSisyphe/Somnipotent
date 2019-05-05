package com.dream.somnipotent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dream.somnipotent.R;

import java.util.ArrayList;

public class LDEInformationAdapter extends ArrayAdapter<LDEInformation> {
    Context context;
    ArrayList<LDEInformation> ItemList;

    public LDEInformationAdapter(@NonNull Context context, ArrayList<LDEInformation> ItemList) {
        super(context, 0,ItemList);
        this.context = context;
        this.ItemList = ItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){//if view null then create new view
            view= LayoutInflater.from(context).inflate(R.layout.other_listview_shape,parent,false);//for creating view
        }

        LDEInformation item = ItemList.get(position);

        //finding listview shape component
        TextView subject = view.findViewById(R.id.subjectListViewShapeId);
        //return super.getView(position, convertView, parent);


        //setting listview shape component to arrryList
        subject.setText(item.getSubject());

        return view;
    }
}
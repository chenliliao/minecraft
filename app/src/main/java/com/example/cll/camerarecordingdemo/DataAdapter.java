package com.example.cll.camerarecordingdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cll on 2017/12/8.
 */

public class DataAdapter extends BaseAdapter {

    private ArrayList<SizeData> data;
    private Context context;
    View v;
    public DataAdapter(Context context,ArrayList<SizeData> data){
        this.data = data;
        this.context = context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.dialog_son_layout, null);;
        TextView textView1 = convertView.findViewById(R.id.textview1);
        TextView textView2 = convertView.findViewById(R.id.textview2);

        textView1.setText(data.get(position).getWidth()+"");
        textView2.setText(data.get(position).getHeight()+"");

        Log.w("TAG","TEST DisplayUtils.getResolution(mCamera,data)  "+data.get(position).getHeight());
        return convertView;
    }
}

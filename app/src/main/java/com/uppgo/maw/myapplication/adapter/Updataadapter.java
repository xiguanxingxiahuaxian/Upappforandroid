package com.uppgo.maw.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uppgo.maw.myapplication.R;

import java.util.List;

/**
 * 项目名称：Upapp
 * 类描述：
 * 创建人：maw@neuqsoft.com
 * 创建时间： 2017/9/8 10:51
 * 修改备注
 */
public class Updataadapter extends BaseAdapter {
    List<String> list;
    private Context context;

    public Updataadapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item,null);
            viewHolder=new ViewHolder();
            viewHolder.title= (TextView) convertView.findViewById(R.id.content);
            viewHolder.position=(TextView)convertView.findViewById(R.id.position);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position)==null?" 暂无具体信息":list.get(position));
        viewHolder.position.setText(position+1+".");

        return convertView;
    }
    class ViewHolder{
        TextView title;
        TextView position;
    }
}
package com.example.sqllite_crud;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> _mDataList;
    private UserSql _userSql;
    private Context context;

    public MyAdapter(ArrayList<HashMap<String, String>> DataList, UserSql userSql, Context context) {
        this._mDataList = DataList;
        this._userSql = userSql;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int i) {
        String FirstName = makeEZ(i, "FirstName");
        String LastName = makeEZ(i, "LastName");
        holder.Name.setText(FirstName + LastName);
        holder.photo.setText(_buildAbbreviation(FirstName, LastName));
        holder.Time.setText(makeEZ(i, "Time"));
    }

    private String makeEZ(int i, String key) {
        return _mDataList.get(_mDataList.size() - 1 - i).get(key);
    }

    @Override
    public int getItemCount() {
        return _mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView photo, Name, Time;
        ImageButton delete, edit;

        public ViewHolder(View inflate) {
            super(inflate);
            photo = inflate.findViewById(R.id.photo);
            Name = inflate.findViewById(R.id.Name);
            Time = inflate.findViewById(R.id.Time);
            delete = inflate.findViewById(R.id.delete);
            edit = inflate.findViewById(R.id.edit);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _userSql.deleteData((makeEZ(getAdapterPosition(), "FirstName")));
                    removeItem(getAdapterPosition());
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _showDialog(getAdapterPosition());
                }
            });
        }
    }

    public void addItem(String firstName, String lastName, UserSql userSql) {

        HashMap<String, String> map = new HashMap<>();
        //在宣告一個map 來put 進去
        map.put("FirstName", firstName);//注意 這裡要跟資料庫的insert 名稱一樣
        map.put("LastName", lastName);
        map.put("Time", _getTime1());
        map.put("Photo", _buildAbbreviation(firstName, lastName));
        userSql.InsertData(firstName, lastName, _getTime1());
        _mDataList.add(map);//把這邊的map 傳給MapList
        notifyDataSetChanged();
        //需要告訴list你需要更新你的畫面//有點類似重整
    }

    public void removeItem(int position) {
        _mDataList.remove(_mDataList.size() - position - 1);//移除MapList
        notifyItemRemoved(position);//刪掉多的畫面更新 有點類似重整
    }

    private String _getTime1() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        Log.e("msg", t1);
        return t1;
    }

    private String _buildAbbreviation(String firstName, String lastName) {
        String photoText = "";
        photoText += firstName.substring(0, 1);
        photoText += ".";
        photoText += lastName.substring(0, 1);
        return photoText;
    }

    private void _showDialog(int i) {
        final EditText firstName, lastName;
        final TextView dialogTitle;
        final Button btnAdd;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialogTitle = dialog.findViewById(R.id.dialogTitle);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        firstName = dialog.findViewById(R.id.firstName);
        lastName = dialog.findViewById(R.id.lastName);
        btnAdd = dialog.findViewById(R.id.add);
        btnAdd.setText("Edit");
        dialogTitle.setText("Edit");
        firstName.setText(_mDataList.get(_mDataList.size() - i - 1).get("FirstName"));
        lastName.setText(_mDataList.get(_mDataList.size() - i - 1).get("LastName"));
        dialog.getWindow().setAttributes(lp);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty()) {
                    _userSql.updateData(firstName.getText().toString(), lastName.getText().toString(), _getTime1(), _mDataList.size() - i);
                    firstName.setText(null);
                    lastName.setText(null);
                    dialog.dismiss();
                    _mDataList = _userSql.getData();
                    notifyDataSetChanged();
                } else {
                    Toast toast = Toast.makeText(context, "此處不能為空白", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });
        dialog.show();
    }
}

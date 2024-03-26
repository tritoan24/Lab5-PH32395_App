package com.ph32395.lab5_ph32395_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSchool extends BaseAdapter {
    private List<School> list;
    private Context context;
    public AdapterSchool(Activity activity, List<School> list) {
        this.context = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size(); // Trả về số lượng phần tử trong danh sách
    }

    @Override
    public Object getItem(int position) {
        return list.get(position); // Trả về đối tượng ở vị trí position
    }

    @Override
    public long getItemId(int position) {
        return position; // Trả về id của đối tượng tại vị trí position
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemschool, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.txtsua = convertView.findViewById(R.id.txtsua);
            holder.txtxoa = convertView.findViewById(R.id.txtxoa);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        School school = list.get(position);
        holder.tvName.setText(school.getName());

        holder.txtxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showDeleteConfirmationDialog(position);
            }
        });
        holder.txtsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSua(position);
            }
        });



        return convertView;
    }

    // ViewHolder pattern để tăng hiệu suất của ListView
    static class ViewHolder {
        TextView tvName,txtsua,txtxoa;
    }


    private void showDeleteConfirmationDialog(final int position) {
        Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                School sinhvien = list.get(position); // Lấy đối tượng SinhvienModel từ danh sách
                String id = sinhvien.get_id();
                ApiSchool apiService = ApiClient.getClient().create(ApiSchool.class);
                Call<Void> call = apiService.xoaSchool(id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            list.remove(position); // Xóa mục khỏi danh sách
                            notifyDataSetChanged(); // Cập nhật giao diện
                            Toast.makeText(context, "Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa sinh viên thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void showDialogSua(final int position) {
        // Lấy đối tượng SinhvienModel từ danh sách
        final School sinhvien = list.get(position);

        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_sua, null); // layout của dialog sửa

        // Khởi tạo các trường nhập liệu trong dialog
        EditText edtName = view.findViewById(R.id.etName);

        // Gán giá trị hiện tại của SinhvienModel cho các trường nhập liệu
        edtName.setText(sinhvien.getName());




        // Thiết lập nút Lưu trong dialog
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = edtName.getText().toString().trim();

                if (newName.isEmpty() ) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }


                sinhvien.setName(newName);

                notifyDataSetChanged();

                // Gọi phương thức cập nhật dữ liệu lên server
                updateSinhvienOnServer(sinhvien);

                // Dismiss dialog
                dialog.dismiss();
            }
        });

        // Thiết lập nút Hủy trong dialog
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Thiết lập layout cho AlertDialog
        builder.setView(view);

        // Tạo AlertDialog từ AlertDialog.Builder
        AlertDialog dialog = builder.create();

        // Hiển thị AlertDialog
        dialog.show();
    }

    private void updateSinhvienOnServer(School sinhvien) {
        // Gọi API cập nhật thông tin sinh viên lên server
        ApiSchool apiService = ApiClient.getClient().create(ApiSchool.class);
        Call<School> call = apiService.suaSchool(sinhvien.get_id(), sinhvien);
        call.enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {
                if (response.isSuccessful()) {
                    // Thông báo cập nhật thành công
                    Toast.makeText(context, "Cập nhật Trường Học thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Thông báo cập nhật thất bại
                    Toast.makeText(context, "Cập nhật Trường Học thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                // Thông báo lỗi khi gọi API
                Toast.makeText(context, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void updateData(List<School> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }
}

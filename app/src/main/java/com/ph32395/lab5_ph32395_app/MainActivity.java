package com.ph32395.lab5_ph32395_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    ListView ls;
    List<School> list;
    AdapterSchool adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ls = findViewById(R.id.lv);

       getSchoolList();
        Button add = findViewById(R.id.btnadd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddSv.class);
                startActivity(i);
            }
        });


        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Nếu người dùng nhấn Enter sau khi nhập từ khóa tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Mỗi khi từ khóa tìm kiếm thay đổi, gọi API tìm kiếm
                if (newText.length() >= 3) { // Tùy chỉnh điều kiện tìm kiếm, ví dụ: tối thiểu 3 ký tự
                    searchSchool(newText);
                }
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Khi người dùng đóng ô tìm kiếm (ấn nút x)
                // Trả về danh sách ban đầu bằng cách gọi API lấy danh sách trường học
                getSchoolList();
                return false;
            }
        });

    }

    private void searchSchool(String keyword) {
        ApiSchool apiSchool = ApiClient.getClient().create(ApiSchool.class);
        Call<List<School>> call = apiSchool.searchSchool(keyword);
        call.enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                if (response.isSuccessful()) {
                    List<School> searchResult = response.body();
                    if (searchResult != null) {
                        adapter.updateData(searchResult); // Cập nhật dữ liệu trong ListView với kết quả tìm kiếm
                    }
                } else {
                    // Xử lý trường hợp không thành công khi gọi API
                    Toast.makeText(MainActivity.this, "Không thể tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                // Xử lý trường hợp lỗi khi gọi API
                Toast.makeText(MainActivity.this, "Lỗi khi tìm kiếm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void getSchoolList() {
        ApiSchool apiSchool = ApiClient.getClient().create(ApiSchool.class);

        Call<List<School>> call = apiSchool.getSchool();
        call.enqueue(new Callback<List<School>>() {
            @Override
            public void onResponse(Call<List<School>> call, Response<List<School>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapter = new AdapterSchool(MainActivity.this, list);
                    ls.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy danh sách trường học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<School>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi khi lấy danh sách trường học: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
package com.ph32395.lab5_ph32395_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSv extends AppCompatActivity {
    private EditText etName;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sv);
        etName = findViewById(R.id.etName);
        btnAdd = findViewById(R.id.btnAdd);
        Button btncancle = findViewById(R.id.btncancle);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();

                // Kiểm tra xem tuổi có phải là số nguyên hay không
                // Kiểm tra xem tất cả các trường đều đã được nhập liệu
                if (name.isEmpty()) {
                    Toast.makeText(AddSv.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }


                School sinhvien = new School(
                        name);
                addSinhvien(sinhvien);

                Intent i = new Intent(AddSv.this,MainActivity.class);
                startActivity(i);
            }
        });


        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addSinhvien(School sinhvien) {
        ApiSchool apiService = ApiClient.getClient().create(ApiSchool.class);
        Call<School> call = apiService.themSchool(sinhvien);

        call.enqueue(new Callback<School>() {
            @Override
            public void onResponse(Call<School> call, Response<School> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddSv.this, "Thêm Trường Học thành công!", Toast.LENGTH_SHORT).show();
                    // Xử lý sau khi thêm thành công
                } else {
                    Toast.makeText(AddSv.this, "Thêm Trường Học thất bại!", Toast.LENGTH_SHORT).show();
                    // Xử lý khi thêm thất bại
                    Log.e("AddSV", "Lỗi khi thêm Trường Học: ");
                }
            }

            @Override
            public void onFailure(Call<School> call, Throwable t) {
                Log.e("AddSV", "Lỗi khi thêm sinh viên: " + t.getMessage());
                Toast.makeText(AddSv.this, "Lỗi khi thêm sinh viên, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
            }


        });

    }
}
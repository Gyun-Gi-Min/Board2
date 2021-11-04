package com.koreait.board;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardModeActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etCtnt;
    private EditText etWriter;
    private BoardService service;
    private int iboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        etTitle = findViewById(R.id.etTitle);
        etCtnt = findViewById(R.id.etCtnt);
        etWriter = findViewById(R.id.etWriter);

        Retrofit retrofit = RetroFitObj.getInstance();
        service = retrofit.create(BoardService.class);

        Intent intent = getIntent();
        iboard = intent.getIntExtra("iboard", 0);

        getBoardDetail();
    }

    public void getBoardDetail() {
        Call<BoardVO> call = service.selBoardDetail(iboard);
        call.enqueue(new Callback<BoardVO>() {
            @Override
            public void onResponse(Call<BoardVO> call, Response<BoardVO> res) {
                if (res.isSuccessful()){
                    // TODO 구현하시오
                    BoardVO vo = res.body();
                    etTitle.setText(vo.getTitle());
                    etCtnt.setText(vo.getCtnt());
                    etWriter.setText(vo.getWriter());

                }else {
                    Log.e("myLog", "통신 오류");
                }
            }

            @Override
            public void onFailure(Call<BoardVO> call, Throwable t) {
                Log.e("myLog", "통신 자체 실패");

            }
        });
    }

    public void clkReg(View v){
        String title = etTitle.getText().toString();
        String ctnt = etCtnt.getText().toString();
        String writer = etWriter.getText().toString();
        AlertDialog.Builder ad = new AlertDialog.Builder(this)
                .setTitle("수정")
                .setMessage("정말 수정하시겠습니가?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Log.i("myLog", "upd-iboard : " + iboard);

                        BoardVO data = new BoardVO();
                        data.setIboard(iboard);
                        data.setTitle(title);
                        data.setTitle(ctnt);
                        data.setTitle(writer);

                        Call<Void> call = service.updBoard(data);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> res) {
                                if (res.isSuccessful()){
                                    Log.i("myLog", "통신 성공");
                                    finish();
                                }else {
                                    Log.e("myLog", "통신 오류");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("myLog", "통신 자체 실패");
                            }
                        });
                    }
                })
                .setNegativeButton("아니오", null);
        ad.create().show();
    }
}
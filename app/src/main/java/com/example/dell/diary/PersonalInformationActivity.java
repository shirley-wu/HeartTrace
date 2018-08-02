package com.example.dell.diary;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.dell.auth.MyAccount;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.dell.diary.DiaryWriteActivity.verifyStoragePermissions;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private EditText chooseBirthday;
    private CircleImageView personalImage;
    private EditText setName;
    private EditText chooseSex;
    private EditText setEmail;
    private EditText setSchool;
    private EditText setSignature;
    private Button personalConfirm;
    private MyAccount myAccount;
    Dialog mCameraDialog;
    Uri mCutUri;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        myAccount = new MyAccount(getApplicationContext());

        setView();
        setListener();
        init();

    }

    private void setView(){
        chooseBirthday = (EditText) findViewById(R.id.choose_birthday);
        personalImage = (CircleImageView) findViewById(R.id.personal_image);
        setName = (EditText) findViewById(R.id.set_name);
        chooseSex = (EditText) findViewById(R.id.choose_sex);
        setEmail = (EditText) findViewById(R.id.set_email);
        setSchool = (EditText) findViewById(R.id.set_school);
        setSignature = (EditText) findViewById(R.id.set_signature);
        personalConfirm = (Button) findViewById(R.id.personal_confirm);
    }

    private void setListener(){
        chooseBirthday.setOnClickListener(this);
        personalImage.setOnClickListener(this);
        setName.setOnClickListener(this);
        chooseSex.setOnClickListener(this);
        setEmail.setOnClickListener(this);
        setSchool.setOnClickListener(this);
        setSignature.setOnClickListener(this);
        personalConfirm.setOnClickListener(this);
    }

    private void init(){
        setName.setCursorVisible(false);
        chooseSex.setCursorVisible(false);
        setEmail.setCursorVisible(false);
        setSchool.setCursorVisible(false);
        setSignature.setCursorVisible(false);

        if(myAccount.getNickname() != null) setName.setText(myAccount.getNickname());
        if(myAccount.getGender() != null) chooseSex.setText(myAccount.getGender());
        if(myAccount.getBirthday() != null) chooseBirthday.setText(myAccount.getBirthday());
        if(myAccount.getEmail() != null) setEmail.setText(myAccount.getEmail());
        if(myAccount.getSchool() != null) setSchool.setText(myAccount.getSchool());
        if(myAccount.getSignature() != null) setSignature.setText(myAccount.getSignature());
        if(myAccount.getHeadimage() == null){
            personalImage.setImageResource(R.drawable.panda);
        }
        else{
            //headImage.setImageResource(imageID);
            //headImage.setImageResource(R.drawable.panda);
            if (myAccount.getHeadimage() != "") {
                byte[] bytes = Base64.decode(myAccount.getHeadimage().getBytes(), 1);
                //  byte[] bytes =headPic.getBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                personalImage.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.choose_birthday:
                TimePickerView pvTime = new TimePickerBuilder(PersonalInformationActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        chooseBirthday.setText(FORMATTER.format(date));
                    }
                })
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLineSpacingMultiplier((float)2.5)
                        .setLabel("","","","","","")//默认设置为年月日时分秒
                        .build();
                pvTime.show();
                break;
            case R.id.set_name:
                setName.setCursorVisible(true);
                break;
            case R.id.choose_sex:
                chooseSex.setCursorVisible(true);
                break;
            case R.id.set_email:
                setEmail.setCursorVisible(true);
                break;
            case R.id.set_school:
                setSchool.setCursorVisible(true);
                break;
            case R.id.set_signature:
                setSignature.setCursorVisible(true);
                break;
            case R.id.personal_image: //打开相册选择图片
                setDialog();
                break;
            case R.id.choose_img:
                verifyStoragePermissions(PersonalInformationActivity.this);
                choseHeadImageFromGallery();
                break;
            case R.id.open_camera:
                choseHeadImageFromCameraCapture();
                break;
            case R.id.cancel:
                mCameraDialog.dismiss();
                break;
            case R.id.personal_confirm:
                myAccount.setNickname(setName.getText().toString());
                myAccount.setGender(chooseSex.getText().toString());
                myAccount.setBirthday(chooseBirthday.getText().toString());
                myAccount.setEmail(setEmail.getText().toString());
                myAccount.setSchool(setSchool.getText().toString());
                myAccount.setSignature(setSignature.getText().toString());
                myAccount.save(true);
                Toast.makeText(PersonalInformationActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);  //貌似这种方法返回的Uri才能用
        intent.setType("image/*");
        startActivityForResult(intent,CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        //创建一个file，用来存储拍照后的照片
        File outputfile = new File(getApplicationContext().getExternalCacheDir(),"output.png");
        try {
            if (outputfile.exists()){
                outputfile.delete();//删除
            }
            outputfile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageuri ;
        if (Build.VERSION.SDK_INT >= 24){
            imageuri = FileProvider.getUriForFile(getApplicationContext(),
                    "com.rachel.studyapp.fileprovider", //可以是任意字符串
                    outputfile);
        }else{
            imageuri = Uri.fromFile(outputfile);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        startActivityForResult(intent,CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CODE_GALLERY_REQUEST:
                    Log.d("headimage", "gallery");
                    //cropRawPhoto(intent.getData());
                    startActivityForResult(CutForPhoto(data.getData()), CODE_RESULT_REQUEST);
                    break;

                case CODE_CAMERA_REQUEST:
                    Log.d("headimage", "camera");
                    String path = getApplicationContext().getExternalCacheDir().getPath();
                    String name = "output.png";
                    startActivityForResult(CutForCamera(path, name), CODE_RESULT_REQUEST);

                    break;

                case CODE_RESULT_REQUEST:
                    Log.d("headimage", "result");
                    try {
                        //获取裁剪后的图片，并显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getApplicationContext().getContentResolver().openInputStream(mCutUri));
                        personalImage.setImageBitmap(bitmap);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        String headPicBase64 = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                        myAccount.setHeadimage(headPicBase64);
                        myAccount.save(true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }
    }

    /**
     * 图片裁剪
     * @param uri
     * @return
     */
    @NonNull
    private Intent CutForPhoto(Uri uri) {
        //直接裁剪
        try{
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置裁剪之后的图片路径文件
        File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                "cutcamera.png"); //随便命名一个
        if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
            cutfile.delete();
        }
        cutfile.createNewFile();
        //初始化 uri
        Uri imageUri = uri; //返回来的 uri
            Log.d("headimage", "imageUri: "+imageUri);
        Uri outputUri = null; //真实的 uri
        Log.d("headimage", "CutForPhoto: "+cutfile);
        outputUri = Uri.fromFile(cutfile);
        mCutUri = outputUri;
        Log.d("headimage", "mCameraUri: "+mCutUri);
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop",true);
        // aspectX,aspectY 是宽高的比例，这里设置正方形
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //设置要裁剪的宽高
        intent.putExtra("outputX", output_X); //200dp
        intent.putExtra("outputY",output_Y);
        intent.putExtra("scale",true);
        //如果图片过大，会导致oom，这里设置为false
        intent.putExtra("return-data",false);
        if (imageUri != null) {
            intent.setDataAndType(imageUri, "image/*");
        }
        if (outputUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }
        intent.putExtra("noFaceDetection", true);
        //压缩图片
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        return intent;
    } catch (IOException e) {
        e.printStackTrace();
    }
        return null;
    }

    /**
     * 拍照之后，启动裁剪
     * @param camerapath 路径
     * @param imgname img 的名字
     * @return
     */
    @NonNull
    private Intent CutForCamera(String camerapath,String imgname) {
        try {
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    "cutcamera.png"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = null; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Intent intent = new Intent("com.android.camera.action.CROP");
            //拍照留下的图片
            File camerafile = new File(camerapath,imgname);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.rachel.studyapp.fileprovider",
                        camerafile);
            } else {
                imageUri = Uri.fromFile(camerafile);
            }
            Log.d("headimage", "CutForPhoto2: "+cutfile);
            Log.d("headimage", "imageUri2: "+imageUri);
            outputUri = Uri.fromFile(cutfile);
            //把这个 uri 提供出去，就可以解析成 bitmap了
            mCutUri = outputUri;
            Log.d("headimage", "mCameraUri2: "+mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", output_X);
            intent.putExtra("outputY",output_Y);
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDialog() {
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.bottom_dialog, null);
        //初始化视图
        root.findViewById(R.id.choose_img).setOnClickListener(this);
        root.findViewById(R.id.open_camera).setOnClickListener(this);
        root.findViewById(R.id.cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
 //       dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

     protected void onDestroy() {
         super.onDestroy();
//         myAccount.setNickname(setName.getText().toString());
//         myAccount.setGender(chooseSex.getText().toString());
//         myAccount.setBirthday(chooseBirthday.getText().toString());
//         myAccount.setEmail(setEmail.getText().toString());
//         myAccount.setSchool(setSchool.getText().toString());
//         myAccount.setSignature(setSignature.getText().toString());
//         myAccount.save();
//         Toast.makeText(PersonalInformationActivity.this, myAccount.getNickname(), Toast.LENGTH_SHORT).show();
     }
}







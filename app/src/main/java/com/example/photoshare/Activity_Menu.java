package com.example.photoshare;

import static com.example.photoshare.constant.Constant_APP.USER_MESSAGE;
import static com.example.photoshare.constant.Constant_APP.USER_PASSWORD;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.photoshare.entity.Entity_Comment;
import com.example.photoshare.entity.Entity_Photo;
import com.example.photoshare.entity.Entity_User;
import com.example.photoshare.interfaces.Interface_MessageSend;
import com.example.photoshare.parse.Response_UserGeneral;
import com.example.photoshare.tool.Tool_SQLiteOpenHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Menu extends AppCompatActivity implements Interface_MessageSend {

    private SQLiteDatabase database;
    private Tool_SQLiteOpenHelper helper;

    /**
     * 用户信息
     */
    private Entity_User user = null;
    private String password = null;

    private void parseUserMessage() {
        Intent intent = getIntent();
        password = intent.getStringExtra(USER_PASSWORD);
        String userMessage = intent.getStringExtra(USER_MESSAGE);
        if (password == null) Log.d("LOG - Intent", " password 为 null");
        if (userMessage == null) Log.d("LOG - Intent", " userMessage 为 null");
        else {
            Gson gson = new Gson();
            Response_UserGeneral userParse = gson.fromJson(userMessage, Response_UserGeneral.class);
            user = userParse.getUser();
        }
    }


    /**
     * Fragment_InformationView - 刷新后的用户信息
     */
    @Override
    public void sendUser(Entity_User user) {
        this.user = user;
    }

    public Entity_User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return user.getId();
    }


    /**
     * Fragment_PhotoExploreList - 用户点击图片的信息
     */
    private Entity_Photo clickPhoto;
    private int clickPosition;

    @Override
    public void sendClickPhoto(Entity_Photo clickPhoto, int clickPosition) {
        this.clickPhoto = clickPhoto;
        this.clickPosition = clickPosition;
    }

    public Entity_Photo getClickPhoto() {
        return clickPhoto;
    }

    public int getClickPhotoPosition() {
        return clickPosition;
    }


    /**
     * Fragment_PhotoExploreList - 刷新后的图片列表信息
     */
    private ArrayList<Entity_Photo> allPhotoList = null;

    @Override
    public void sendAllPhotoList(ArrayList<Entity_Photo> allPhotoList) {
        this.allPhotoList = allPhotoList;
        if (allPhotoList != null) helper.refreshTable(database, allPhotoList);
    }

    @Override
    public void setPhotoLikeState(int position, boolean hasLike) {
        Entity_Photo photo = allPhotoList.get(position);
        int photoLikeNum = photo.getLikeNum();
        if (allPhotoList != null) {
            photo.setHasLike(hasLike);
            if (hasLike) photo.setLikeNum(photoLikeNum + 1);
            else photo.setLikeNum(photoLikeNum - 1);
            helper.refreshTable(database, allPhotoList);
        }
    }

    @Override
    public void setPhotoCollectState(int position, boolean hasCollect) {
        Entity_Photo photo = allPhotoList.get(position);
        int photoCollectNum = photo.getCollectNum();
        if (allPhotoList != null) {
            photo.setHasCollect(hasCollect);
            if (hasCollect) photo.setCollectNum(photoCollectNum + 1);
            else photo.setCollectNum(photoCollectNum - 1);
            helper.refreshTable(database, allPhotoList);
        }
    }

    public ArrayList<Entity_Photo> getAllPhotoList() {
        return allPhotoList;
    }


    /**
     * Fragment_PhotoExploreList - 被查询到的图文ID列表
     */
    private ArrayList<Entity_Photo> quirePhotoList = null;

    @Override
    public void sendQuireContent(ArrayList<Entity_Photo> quirePhotoList) {
        this.quirePhotoList = quirePhotoList;
    }

    public ArrayList<Entity_Photo> getQuirePhotoList() {
        return quirePhotoList;
    }


    /**
     * Fragment_Share - 上传图片的 URI
     */
    private Uri photoUri;

    @Override
    public void sendPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }


    /**
     * Fragment_CommentFirst - 被点击的一级评论
     */
    private Entity_Comment clickFirstComment;

    @Override
    public void sendClickFirstComment(Entity_Comment clickFirstComment) {
        this.clickFirstComment = clickFirstComment;
    }

    public Entity_Comment getClickFirstComment() {
        return clickFirstComment;
    }


    /**
     * Fragment_CommentFirst - 已经查看图片的一级评论列表集合
     */
    private final HashMap<String, StringBuffer> firstCommentList = new HashMap<>();

    @Override
    public void sendFirstCommentList(StringBuffer photoFirstCommentList, String photoId) {
        firstCommentList.put(photoId, photoFirstCommentList);
    }

    public StringBuffer getFirstCommentList(String photoId) {
        return firstCommentList.get(photoId);
    }


    /**
     * Fragment_CommentFirst - 已经查看图片的二级评论列表集合
     */
    private final HashMap<String, StringBuffer> secondCommentList = new HashMap<>();

    @Override
    public void sendSecondCommentList(StringBuffer photoSecondCommentList, String photoId, String firstComment) {
        String secondCommentKey = photoId + firstComment;
        secondCommentList.put(secondCommentKey, photoSecondCommentList);
    }

    public StringBuffer getSecondCommentList(String photoId, String firstComment) {
        return secondCommentList.get(photoId + firstComment);
    }


    /**
     * Fragment_PhotoDetails - 点击图片的序号
     */
    private int viewPagePosition;

    @Override
    public void sendViewPagerClickPosition(int position) {
        this.viewPagePosition = position;
    }

    public int getClickPosition() {
        return viewPagePosition;
    }


    /**
     * Fragment_ShareUploadPhoto - 传送上传成功的 imageCode、imageFileList
     */
    private String imageCode;
    private ArrayList<File> imageFileList;

    @Override
    public void sendImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    @Override
    public void sendImageFileList(ArrayList<File> imageFileList) {
        this.imageFileList = imageFileList;
    }

    public String getImageCode() {
        return imageCode;
    }

    public ArrayList<File> getImageFileList() {
        return imageFileList;
    }


    /**
     * Fragment_AvatarChoose - 用户选择的头像图片Uri
     */
    private String modifyAvatar = null;

    @Override
    public void sendModifyAvatar(String modifyAvatar) {
        this.modifyAvatar = modifyAvatar;
    }

    public String getModifyAvatar() {
        return modifyAvatar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseUserMessage();
        helper = new Tool_SQLiteOpenHelper(Activity_Menu.this);
        database = helper.getWritableDatabase();
        allPhotoList = helper.getDataFromTable(database);
        setContentView(R.layout.activity_menu);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // 构建AppBarConfiguration，传入一组顶级目的地 ID 传递给构造函数
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.navigation_home, R.id.navigation_photo_explore, R.id.navigation_photo_share).build();
        // navController 用于管理页面的导航和切换
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // AppBarConfiguration 用于App bar的配置，NavController用于页面的导航和切换，将 App bar 和 NavController 绑定起来
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // BottomNavigationView 为底部导航栏控件，NavController用于页面的导航和切换，将 BottomNavigationView 和 NavController 绑定起来
        NavigationUI.setupWithNavController(navView, navController);

        getSupportActionBar().hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
        database.close();
    }
}
package com.example.photoshare.constant;

public final class Constant_APP {

    /* intent 传递标志 */
    /**
     * 用户密码 标志
     */
    public static String USER_PASSWORD = "com.PhotoShare.intent.password";
    /**
     * 用户账号 标志
     */
    public static String USER_USERNAME = "com.PhotoShare.intent.userName";
    /**
     * 用户ID 标志
     */
    public static String USER_ID = "com.PhotoShare.intent.userId";
    /**
     * 用户信息 标志
     */
    public static String USER_MESSAGE = "com.PhotoShare.intent.userMessage";


    /* 其他 */
    public static int MAN = 1;
    public static int WOMAN = 2;
    public static int SECRET = 3;



    /* Id */
    /**
     * PhotoShare
     */
    public static String APP_ID_KEEP = "28f6422b72374dc193ce88d5c9ed3d03";
    public static String APP_SECRET_KEEP = "98975c86b769ef3884396a3c2aa6e3d402b47";

    /**
     * 测试
     */
    public static String APP_ID_TEST = "f96bf489a7fa4ee991cd7d0ef1e069ed";
    public static String APP_SECRET_TEST = "25054a88208f79a564c16b801dff2932eda37";

    /**
     * 备用
     */
    public static String APP_ID_STANDBY = "8e9a031b01df43b2a6a5d9947adef893";
    public static String APP_SECRET_STANDBY = "89688e526f145d67b41ac8e567152f9e5f668";

    /**
     * 当前
     */
    public static String APP_ID = APP_ID_TEST;
    public static String APP_SECRET = APP_SECRET_TEST;

    /**
     * 天行API appKey
     */
    public static String TIAN_APP_KET = "47add00148ffb647d94adb74e758fe28";


    /* 网址 */
    /**
     * 服务器网址
     */
    public static String SERVER_URL = "http://47.107.52.7:88/member/photo";
    /**
     * 天行API附加接口网址
     */
    public static String TIAN_URL = "http://api.tianapi.com";


    /* 朋友圈文案 */
    /**
     * 随机获取一条朋友圈文案 GET
     */
    public static String FRIEND_CIRCLE_GET_ERL = TIAN_URL + "/pyqwenan/index";


    /* 用户 */
    /**
     * 用户登录 POST
     */
    public static String USER_LOGIN_POST_URL = SERVER_URL + "/user/login";
    /**
     * 用户注册 POST
     */
    public static String USER_REGISTER_POST_URL = SERVER_URL + "/user/register";
    /**
     * 用户修改信息 POST
     */
    public static String USER_UPDATE_POST_URL = SERVER_URL + "/user/update";

    /* 上传 */
    /**
     * 上传文件 POST
     */
    public static String IMAGE_UPLOAD_POST_URL = SERVER_URL + "/image/upload";

    /* 分享 */
    /**
     * 获取图片分享发现列表 GET
     */
    public static String SHARE_GET_URL = SERVER_URL + "/share";
    /**
     * 新增图文分享 POST
     */
    public static String SHARE_ADD_POST_URL = SERVER_URL + "/share/add";
    /**
     * 删除图文分享 POST
     */
    public static String SHARE_DELETE_POST_URL = SERVER_URL + "/share/delete";
    /**
     * 获取单个图文分享的详情 GET
     */
    public static String SHARE_DETAIL_GET_URL = SERVER_URL + "/share/detail";
    /**
     * 获取我的动态图片分享列表 GET
     */
    public static String SHARE_MYSELF_GET_URL = SERVER_URL + "/share/myself";

    /* 点赞 */
    /**
     * 用户对图文分享进行点赞 POST
     */
    public static String LIKE_POST_URL = SERVER_URL + "/like";
    /**
     * 用户取消对图文分享的点赞 POST
     */
    public static String LIKE_CANCEL_POST_URL = SERVER_URL + "/like/cancel";
    /**
     * 获取当前登录用户点赞图文列表 GET
     */
    public static String LIKE_GET_URL = SERVER_URL + "/like";

    /* 收藏 */
    /**
     * 用户对图文分享进行收藏 POST
     */
    public static String COLLECT_POST_URL = SERVER_URL + "/collect";
    /**
     * 用户取消对图文分享的收藏 POST
     */
    public static String COLLECT_CANCEL_POST_URL = SERVER_URL + "/collect/cancel";
    /**
     * 获取当前登录用户收藏图文列表 GET
     */
    public static String COLLECT_GET_URL = SERVER_URL + "/collect";

    /* 评论 */
    /**
     * 获取一级评论 GET
     */
    public static String COMMENT_FIRST_GET_URL = SERVER_URL + "/comment/first";
    /**
     * 新增一个图片分享的一级评论或回复 POST
     */
    public static String COMMENT_FIRST_POST_URL = SERVER_URL + "/comment/first";
    /**
     * 获取二级评论 GET
     */
    public static String COMMENT_SECOND_GET_URL = SERVER_URL + "/comment/second";
    /**
     * 新增一个图片分享的二级评论或回复 POST
     */
    public static String COMMENT_SECOND_POST_URL = SERVER_URL + "/comment/second";

    /* 关注 */
    /**
     * 添加关注 POST
     */
    public static String FOCUS_POST_URL = SERVER_URL + "/focus";
    /**
     * 取消关注 POST
     */
    public static String FOCUS_CANCEL_POST_URL = SERVER_URL + "/focus/cancel";
    /**
     * 获取当前登录用户已关注的图文列表 GET
     */
    public static String FOCUS_GET_URL = SERVER_URL + "/focus";
}
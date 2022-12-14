package com.example.photoshare.menu.explore;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static com.example.photoshare.customize.Customize_Animator.HIDE_VIEW;
import static com.example.photoshare.customize.Customize_Animator.SHOW_VIEW;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.photoshare.Activity_Menu;
import com.example.photoshare.R;
import com.example.photoshare.constant.Constant_APP;
import com.example.photoshare.customize.Customize_Animator;
import com.example.photoshare.entity.Entity_Photo;
import com.example.photoshare.interfaces.Interface_ClickViewSend;
import com.example.photoshare.interfaces.Interface_MessageSend;
import com.example.photoshare.parse.Request_Interceptor;
import com.example.photoshare.parse.Response_PhotoList;
import com.example.photoshare.tool.Tool_SQLiteOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fragment_PhotoExploreBig extends Fragment {

    /* ?????? */
    /**
     * ??????
     */
    private Customize_Animator customizeAnimator;
    /**
     * ????????????
     */
    private SwipeRefreshLayout swipe;
    /**
     * ??????
     */
    private RecyclerView rvPhotoList;
    /**
     * ?????????
     */
    private CardView cvQuireBar;
    /**
     * ????????? ?????????
     */
    private EditText etQuire;
    /**
     * ?????????????????????
     */
    private RelativeLayout rlQuireMask;
    /**
     * ???????????????
     */
    private Spinner spQuire;
    /**
     * ??????????????????
     */
    private FloatingActionButton fabScrollTop;


    /* ?????? */
    /**
     * ????????????
     */
    private final String quireTitle = "??????";
    /**
     * ????????????
     */
    private final String quireContent = "??????";
    /**
     * ???????????????
     */
    private ArrayList<String> quireTypeList;
    /**
     * ????????????
     */
    private String quireType = quireTitle;
    /**
     * ?????????????????? ID
     */
    private String userId = null;
    /**
     * ??? Activity_Menu ????????????????????????
     */
    private ArrayList<Entity_Photo> photoListSend = null;
    /**
     * ??? Activity_Menu ????????????????????????
     */
    private ArrayList<Entity_Photo> photoListGet = null;
    /**
     * ???????????????
     */
    private Tool_SQLiteOpenHelper helper;
    /**
     * ?????????
     */
    private SQLiteDatabase database;
    /**
     * ????????????
     */
    private final List<Entity_Photo> photoList = new ArrayList<>();


    /* ?????? */
    private Interface_MessageSend interface_messageSend;


    /* ????????? */
    /**
     * ???????????????
     */
    private final int HIDE_KEYBOARD = 1;
    /**
     * ???????????????
     */
    private final int SHOW_KEYBOARD = 2;


    /* ????????? */
    private Adapter_PhotoExploreBig photoAdapter;


    /* ???????????? */

    /**
     * ????????????
     */
    private void networkRequest() {
        new Thread(() -> {
            Request request;
            String urlParam = "?" + "size=80" + "&" + "userId=" + userId;
            request = new Request.Builder().url(Constant_APP.SHARE_GET_URL + urlParam).get().build();
            try {
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Request_Interceptor()).build();
                client.newCall(request).enqueue(photoShowCallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * ???????????? ????????????????????????
     */
    private final okhttp3.Callback photoShowCallback = new okhttp3.Callback() {
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("LOG - ????????????", "????????? : " + responseBody);
                addNetDataInAdapter(responseBody);
            }
        }

        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            e.printStackTrace();
        }
    };

    /**
     * ?????? ???????????? ???????????????
     *
     * @param photoList ????????????
     */
    @SuppressLint("NotifyDataSetChanged")
    private void addLocalDataInAdapter(ArrayList<Entity_Photo> photoList) {
        if (!this.photoList.isEmpty()) this.photoList.clear();
        for (Entity_Photo photo : photoList) {
            this.photoList.add(photo);
            photoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param body ??????????????????
     */
    private void addNetDataInAdapter(String body) {
        new Thread(() -> {
            Gson gson = new Gson();
            Response_PhotoList responseParse = gson.fromJson(body, Response_PhotoList.class);
            if (responseParse.getCode() == 5311) jumpErrorHandler.sendMessage(new Message());
            else {
                if (responseParse.getData() != null) {
                    photoListSend = responseParse.getData().getRecords();
                    interface_messageSend.sendAllPhotoList(photoListSend);
                    for (Entity_Photo photo : photoListSend) {
                        Message addMessage = new Message();
                        addMessage.obj = photo;
                        addPhotoHandler.sendMessage(addMessage);
                    }
                }
            }
        }).start();
    }

    /**
     * ???????????? - ??????????????????????????????
     */
    private final Handler addPhotoHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message msg) {
            photoList.add((Entity_Photo) msg.obj);
            photoAdapter.notifyDataSetChanged();
        }
    };
    /**
     * ???????????? - ???????????????????????????
     */
    private final Handler jumpErrorHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_PhotoExploreBig_to_fragment_Error);
        }
    };




    /* ????????? */
    /**
     * ????????? ????????????
     */
    private final View.OnClickListener ivJumpListListener = v ->
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_PhotoExploreBig_to_navigation_photo_explore);
    /**
     * ????????????????????????
     */
    private final Interface_ClickViewSend viewSendListener = new Interface_ClickViewSend() {
        @Override
        public void onItemClick(View view, int position) {
            Entity_Photo photo = photoList.get(position);
            interface_messageSend.sendClickPhoto(photo, position);
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_PhotoExploreBig_to_fragment_PhotoDetails);
        }
    };
    /**
     * ???????????????????????? ?????????????????????
     */
    private final RecyclerView.OnScrollListener rvPhotoListScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == SCROLL_STATE_IDLE) {
                // ???????????????item??????????????????????????????????????????????????????
                if (recyclerView.getLayoutManager().findViewByPosition(0) != null) {
                    if (fabScrollTop.getVisibility() == View.VISIBLE) {
                        // ?????????????????????????????????
                        customizeAnimator.setSlideFadeAnimator(fabScrollTop, HIDE_VIEW);
                    }
                } else {
                    if (fabScrollTop.getVisibility() == View.INVISIBLE) {
                        // ??????????????????????????????
                        customizeAnimator.setSlideFadeAnimator(fabScrollTop, SHOW_VIEW);
                    }
                }
            }
        }
    };
    /**
     * ???????????????
     */
    private final View.OnClickListener fabScrollTopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rvPhotoList.smoothScrollToPosition(0);
        }
    };
    /**
     * ??????????????????
     */
    private final SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onRefresh() {
            if (photoAdapter.getItemCount() != 0) {
                photoList.clear();
                photoAdapter.notifyDataSetChanged();
            }
            networkRequest();
            photoAdapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        }
    };
    /**
     * ????????????????????????  Get
     */
    private final View.OnClickListener rlShowQuireListener = v -> {
        if (rlQuireMask.getAlpha() == 0f || cvQuireBar.getAlpha() == 0f) {
            setKeyboardState(etQuire, SHOW_KEYBOARD);
            setQuireBarState(SHOW_VIEW);
        } else {
            setKeyboardState(etQuire, HIDE_KEYBOARD);
            setQuireBarState(HIDE_VIEW);
        }
    };
    /**
     * ?????? ?????????????????? ??????????????????
     */
    private final View.OnClickListener rlQuireMaskListener = v -> {
        setKeyboardState(etQuire, HIDE_KEYBOARD);
        setQuireBarState(HIDE_VIEW);
    };
    /**
     * ????????? ???????????????
     */
    private final TextView.OnEditorActionListener etQuireListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (quireType.equals(quireTitle))
                searchQuireContent(quireTitle);
            else
                searchQuireContent(quireContent);
        }
        return false;
    };
    /**
     * ??????????????????
     */
    private final AdapterView.OnItemSelectedListener spQuireListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            quireType = quireTypeList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    /**
     * ?????? SQLite ??????????????????
     */
    private void searchQuireContent(String type) {
        String quireString = etQuire.getText().toString();
        if (!quireString.equals("")) {
            ArrayList<Entity_Photo> list;
            if (type.equals(quireTitle))
                list = helper.quireTitleFromTable(database, quireString);
            else
                list = helper.quireContentFromTable(database, quireString);

            if (list != null) {
                interface_messageSend.sendQuireContent(list);
                setKeyboardState(etQuire, HIDE_KEYBOARD);
                Navigation.findNavController(requireView()).navigate(R.id.action_fragment_PhotoExploreBig_to_fragment_PhotoQuire);
            } else {
                Toast.makeText(requireContext(), "????????????????????????,?????????????????????", Toast.LENGTH_SHORT).show();
            }
            etQuire.setText("");
        } else {
            Toast.makeText(requireContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * ?????? ????????? ?????????????????????
     *
     * @param state ????????? ??????
     */
    private void setQuireBarState(int state) {
        customizeAnimator.setFadeAnimator(rlQuireMask, state);
        customizeAnimator.setFadeAnimator(cvQuireBar, state);
    }


    /**
     * ?????? ??????????????? ?????????????????????
     *
     * @param state    ??????????????? ??????
     * @param editText ????????????????????????????????????
     */
    private void setKeyboardState(EditText editText, int state) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (state == HIDE_KEYBOARD) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } else if (state == SHOW_KEYBOARD) {
                editText.requestFocus();
                imm.showSoftInput(editText, 0);
            }
        }
    }


    private void bindView(View root) {
        rvPhotoList = root.findViewById(R.id.rv_photo_explore_big);
        rvPhotoList.addOnScrollListener(rvPhotoListScrollListener);

        fabScrollTop = root.findViewById(R.id.rl_photo_explore_big_scroll_top);
        fabScrollTop.setVisibility(View.INVISIBLE);
        fabScrollTop.setOnClickListener(fabScrollTopListener);

        swipe = root.findViewById(R.id.sw_photo_explore_big_swipe);
        swipe.setOnRefreshListener(swipeListener);

        // ?????????????????????
        RelativeLayout rlShowQuire = root.findViewById(R.id.rl_photo_explore_big_show_quire_button);
        rlShowQuire.setOnClickListener(rlShowQuireListener);

        // ?????????
        cvQuireBar = root.findViewById(R.id.cv_photo_explore_big_quire_bar);
        cvQuireBar.setVisibility(View.INVISIBLE);

        // ????????? ??????????????????
        etQuire = root.findViewById(R.id.et_photo_explore_big_quire);
        etQuire.setOnEditorActionListener(etQuireListener);

        // ????????? ????????????
        spQuire = root.findViewById(R.id.sp_photo_explore_big_quire_spinner);

        // ????????????????????????
        rlQuireMask = root.findViewById(R.id.rl_photo_explore_big_quire_mask);
        rlQuireMask.setVisibility(View.INVISIBLE);
        rlQuireMask.setOnClickListener(rlQuireMaskListener);

        RelativeLayout rlConvert = root.findViewById(R.id.rl_photo_explore_big_convert);
        rlConvert.setOnClickListener(ivJumpListListener);
    }

    private void setData(Context context) {
        customizeAnimator = new Customize_Animator();
        // ????????????
        photoListGet = ((Activity_Menu) context).getAllPhotoList();
        photoAdapter = new Adapter_PhotoExploreBig(requireContext(), photoList);
        photoAdapter.setOnPhotoClickListener(viewSendListener);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvPhotoList.setAdapter(photoAdapter);
        rvPhotoList.setLayoutManager(layoutManager);

        if (photoListGet != null) {
            addLocalDataInAdapter(photoListGet);
        } else networkRequest();

        // ?????????
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_head, quireTypeList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spQuire.setAdapter(adapter);
        spQuire.setDropDownVerticalOffset(125);
        spQuire.setDropDownHorizontalOffset(-40);
        spQuire.getBackground().setColorFilter(getResources().getColor(R.color.white_c), PorterDuff.Mode.SRC_ATOP);
        spQuire.setOnItemSelectedListener(spQuireListener);

        // ?????????
        helper = new Tool_SQLiteOpenHelper(requireContext());
        database = helper.getWritableDatabase();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo_explore_big, container, false);
        bindView(root);
        setData(getActivity());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        photoList.clear();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userId = ((Activity_Menu) context).getUserId();
        try {
            interface_messageSend = (Interface_MessageSend) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Interface not implemented");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ?????????
        quireTypeList = new ArrayList<>();
        quireTypeList.add(quireTitle);
        quireTypeList.add(quireContent);

        // ?????????
        helper = new Tool_SQLiteOpenHelper(requireContext());
        database = helper.getWritableDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        database.close();
    }
}
package com.example.kim.finalprojectowner1.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kim.finalprojectowner1.MainActivity;
import com.example.kim.finalprojectowner1.R;
import com.example.kim.finalprojectowner1.map.MapActivity;
import com.example.kim.finalprojectowner1.model.ChatModel;
import com.example.kim.finalprojectowner1.model.NotificationModel;
import com.example.kim.finalprojectowner1.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.remotemonster.sdk.Config;
import com.remotemonster.sdk.PercentFrameLayout;
import com.remotemonster.sdk.RemonCall;
import com.remotemonster.sdk.core.SurfaceViewRenderer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {

    Config config;
    public final String REST_HOST = "https://signal.remotemonster.com/rest/";
    public final String WSS_HOST = "wss://signal.remotemonster.com/ws";
    private String destinatonUid;

    private Button button;
    private EditText editText;
    private Button btn_second;
    private String uid;
    private String chatRoomUid;

    private RemonCall remonCall;


    SurfaceViewRenderer surfRendererLocal;
    PercentFrameLayout perFrameLocal;
    SurfaceViewRenderer surfRendererRemote;
    PercentFrameLayout perFrameRemote;
    //영상통화

    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private UserModel destinationUserModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);

        btn_second = (Button) findViewById(R.id.btn_second);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID
        destinatonUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
        button = (Button) findViewById(R.id.messageActivity_button);
        editText = (EditText) findViewById(R.id.messageActivity_editText);

        recyclerView = (RecyclerView) findViewById(R.id.messageActivity_reclclerview);

        surfRendererLocal = (SurfaceViewRenderer) findViewById(R.id.surfRendererLocal);
        perFrameLocal = (PercentFrameLayout) findViewById(R.id.perFrameLocal);
        surfRendererRemote = (SurfaceViewRenderer) findViewById(R.id.surfRendererRemote);
        perFrameRemote = (PercentFrameLayout) findViewById(R.id.perFrameRemote);





        remonCall = RemonCall.builder()
                .context(MessageActivity.this)
                .audioType("music")
                .localView(surfRendererLocal)
                .remoteView(surfRendererRemote)
                .serviceId(getConfig().getServiceId())
                .key(getConfig().getKey())
                .restUrl(getConfig().restHost)
                .wssUrl(getConfig().socketUrl)
                .build();


        remonCall.connect("123456");


        btn_second.setOnClickListener(v -> {
            startActivity(new Intent(MessageActivity.this, MapActivity.class));
        });


        button.setOnClickListener(v -> {

            ChatModel chatModel = new ChatModel();
            chatModel.users.put(uid, true);
            chatModel.users.put(destinatonUid, true);
            if (chatRoomUid == null) {
                //채팅방 아이디가 널이였을 때 방 생성 시킨다.
                button.setEnabled(false);
                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(aVoid -> checkChatRoom());
            } else {
                //채팅방 아이디가 널이 아니면 데이터 값만 넣어준다.
                ChatModel.Comment comment = new ChatModel.Comment();
                comment.uid = uid;
                comment.message = editText.getText().toString();
                //채팅창에서 내용을 가져온다.
                comment.timestamp = ServerValue.TIMESTAMP;
                //데이터베이스에서 시간을 가져온다
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(task -> {
                    sendGcm();
                    editText.setText("");
                });
            }
        });

        checkChatRoom();
    }

    void sendGcm(){

        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destinationUserModel.pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.text = editText.getText().toString();


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyDPLHZ2VMSx8kmrMW-WHSAcOAJCbGFk2WA")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    void checkChatRoom() {
        //중복 확인 해주는 메소드
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    //user의 값을 받아오는 부분
                    if (chatModel.users.containsKey(destinatonUid)) {
                        chatRoomUid = item.getKey();//방아이디
                        button.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void chatting(View view) {
        return;
    }





    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(destinatonUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    destinationUserModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            getMessageList();

        }

        void getMessageList() {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();
                    //데이터가 계속 쌓이기 때문에 초기화를 시켜주지 않으면 그전꺼 까지 날라가게되서 안됨
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    //메세지가 갱신
                    notifyDataSetChanged();
                    recyclerView.scrollToPosition(comments.size() - 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);


            //내가보낸 메세지
            if (comments.get(position).uid.equals(uid)) {
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.END);
            } else {
                messageViewHolder.textview_name.setText(destinationUserModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.START);

            }
            //timestamp 에서 찍힌 밀리세컨드를 현제 시간형태로 바꿔주는 역활
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_timestamp.setText(time);
        }


        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textview_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textView_message);
                textview_name = (TextView) view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView) view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linearLayout_destination);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.messageItem_linearLayout_main);
                textView_timestamp = (TextView) view.findViewById(R.id.messageItem_textview_timestamp);
            }
        }

    }


    public Config getConfig() {
        config = new Config();
        config.setVideoCodec("VP8");
        config.setRestHost(REST_HOST);
        config.setSocketUrl(WSS_HOST);
        config.setVideoWidth(640);
        config.setVideoHeight(480);
        config.setVideoFps(30);
        config.setStartVideoBitrate(1000);
        config.setAudioStartBitrate(32);
        config.setStatInterval(2000);
        config.setVideoCall(true);
        config.setLogLevel(Log.INFO);
        config.setKey("4577d4fc8f40d36a0f8aa473d4faa91c70e6b0caace923b1");
        config.setServiceId("youngmeen24@gmail.com");
        return config;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        remonCall.close();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
        finish();
    }
}

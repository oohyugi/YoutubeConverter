package in.chroot.convertyoutube;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.ShareCompat.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.deeplinkdispatch.DeepLink;

import java.util.ArrayList;
import java.util.List;

import in.chroot.convertyoutube.api.ApiClient;
import in.chroot.convertyoutube.api.MyObservable;
import in.chroot.convertyoutube.api.VideoMdl;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@DeepLink({"https://m.youtube.com/watch?v={id}", "https://youtube.com/watch?v={id}"})
public class MainActivity extends AppCompatActivity {
    ApiClient mClient;
    EditText edUrl;
    Button btConvert;
    String yourUrl;
    VideoView mVideo;
    WebView webView;
    LinearLayout btnFormat;
    TextView tvFormat;
    ConverterAdapter adapter;
    LinearLayout progressBar;
    Button btnUnduh;
    ImageView btnPlay;
    List<VideoMdl.DATA> list = new ArrayList<>();
    String urlVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mClient = new ApiClient();

        Intent intent = getIntent();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edUrl = (EditText) findViewById(R.id.edUrl);
        btConvert = (Button) findViewById(R.id.btConvert);
        btnFormat = (LinearLayout) findViewById(R.id.btnFormat);
        tvFormat = (TextView) findViewById(R.id.tvFormat);
        mVideo = (VideoView) findViewById(R.id.videoView);
        progressBar = (LinearLayout) findViewById(R.id.progress);
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
        btnUnduh = (Button) findViewById(R.id.btnUnduh);
        webView = (WebView) findViewById(R.id.webView);

        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = intent.getExtras();
            String idString = parameters.getString("v");


            yourUrl = "https://m.youtube.com/watch?v=" + idString;
            edUrl.setText(yourUrl);
            progressBar.setVisibility(View.VISIBLE);
            callApi(yourUrl);

        }
        btnUnduh.setEnabled(false);


        btnUnduh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(urlVideo);
            }
        });


        btConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                yourUrl = edUrl.getText().toString();
                callApi(yourUrl);
            }
        });

    }


    private void callApi(String yourUrl) {
        mClient.getmApiService().getVideo(yourUrl, getResources().getString(R.string.apikey))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObservable<VideoMdl>() {
                    @Override
                    protected void onSuccess(VideoMdl videoMdls) {
                        progressBar.setVisibility(View.GONE);
                        btnUnduh.setEnabled(true);
                        Log.e("onSuccess: ", videoMdls.data.get(0).link);

                        list = videoMdls.data;

                        btnFormat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialogFormat(list);
                            }
                        });
                        tvFormat.setText(list.get(0).format + " " + list.get(0).quality);
                        urlVideo = list.get(0).link;
                        btnPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                putarVideo(urlVideo);
                                btnPlay.setVisibility(View.GONE);
                            }
                        });


                    }


                    @Override
                    protected void onError(String message) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        Log.e("onError: ", message);

                    }
                });
    }

    private void showDialogFormat(List<VideoMdl.DATA> list) {
        mVideo.stopPlayback();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_format);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycleview);

        configRecycleView(list, recyclerView, dialog);


        dialog.show();

    }

    private void configRecycleView(List<VideoMdl.DATA> list, RecyclerView recyclerView, final Dialog dialog) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        adapter = new ConverterAdapter(this, list, new ConverterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, VideoMdl.DATA model) {

                btnPlay.setVisibility(View.VISIBLE);
                dialog.dismiss();
                urlVideo = model.link;
                tvFormat.setText(model.format + " " + model.quality);


            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }

    private void putarVideo(final String link) {
        try {


            MediaController mediaController = new MediaController(MainActivity.this);
            mediaController.setAnchorView(mVideo);
            mVideo.setMediaController(mediaController);
            String url = link.replace(" ", "");
            Uri uri = Uri.parse(url);
            mVideo.setVideoURI(uri);
            mVideo.start();
        } catch (Exception e) {

            Log.e("putarVideo: ", e.getMessage());
            e.printStackTrace();

        }

    }


}

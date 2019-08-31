package com.example.codetrack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.MyViewHolder> {
    private List<Contest> mList;
    Context mContext;
    private Contest mModel;
    private RecyclerView recyclerView;

    private static final int UNSELECTED = -1;

    private int selectedItem = UNSELECTED;
    //Custom tab
    private CustomTabsServiceConnection customTabsServiceConnection;
    private CustomTabsClient mClient;
    CustomTabsSession customTabsSession;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textStartTime, textStartTimeHead, textDuration, textDurationHead, textEndTime, textEndTimeHead;
        RelativeLayout layoutCard;
        ExpandableLayout expandableLayout;
        ImageButton imageLink, imageShare;
        ImageView platformImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textStartTime = itemView.findViewById(R.id.text_start_time);
            textDuration = itemView.findViewById(R.id.text_duration);
            textEndTime = itemView.findViewById(R.id.text_end_time);
            textStartTimeHead = itemView.findViewById(R.id.text_start_time_head);
            textEndTimeHead = itemView.findViewById(R.id.text_end_time_head);
            textDurationHead = itemView.findViewById(R.id.text_duration_head);
            layoutCard = itemView.findViewById(R.id.layout_card);
            expandableLayout = itemView.findViewById(R.id.sub_item);
            platformImage = itemView.findViewById(R.id.image_platform);

            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                @Override
                public void onExpansionUpdate(float expansionFraction, int state) {
                    Log.d("Expandable layout", "onExpansionUpdate: " + state);
                    if (state == ExpandableLayout.State.EXPANDING) {
                        recyclerView.smoothScrollToPosition(getAdapterPosition());
                    }
                }
            });

            imageLink = itemView.findViewById(R.id.image_link);
            imageShare = itemView.findViewById(R.id.image_share);
            layoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyViewHolder holder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                    if (holder != null) {
                        holder.layoutCard.setSelected(false);
                        holder.expandableLayout.collapse();
                        holder.expandableLayout.setVisibility(View.GONE);
                    }
                    int pos = getAdapterPosition();

                    if (pos == selectedItem) {
                        selectedItem = UNSELECTED;
                        expandableLayout.setVisibility(View.GONE);
                    } else {
                        layoutCard.setSelected(true);
                        expandableLayout.setVisibility(View.VISIBLE);
                        expandableLayout.expand();
                        selectedItem = pos;
                    }
                }
            });

        }
    }

    ContestAdapter(List<Contest> list, Context context, RecyclerView recyclerView) {
        this.mList = list;
        this.mContext = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ContestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        mModel = mList.get(position);

        if (mModel.getPlatform().equalsIgnoreCase("codechef")) {
            holder.platformImage.setBackgroundResource(R.drawable.codechef);
        } else if (mModel.getPlatform().equalsIgnoreCase("codeforces")) {
            holder.platformImage.setBackgroundResource(R.drawable.codeforces);
        } else if (mModel.getPlatform().equalsIgnoreCase("topcoder")) {
            holder.platformImage.setBackgroundResource(R.drawable.topcoder);
        } else if (mModel.getPlatform().equalsIgnoreCase("hackerearth")) {
            holder.platformImage.setBackgroundResource(R.drawable.hackerearthlogo);
        } else if (mModel.getPlatform().equalsIgnoreCase("hackerrank")) {
            holder.platformImage.setBackgroundResource(R.drawable.hackerrank);
        }

        Log.i("URL DISPLAY", mModel.getLink());


        holder.textName.setText(mModel.getName());
        if (mModel.getStartTime().equals("")) {
            holder.textStartTime.setVisibility(View.GONE);
            holder.textStartTimeHead.setVisibility(View.GONE);
        } else {
            holder.textStartTime.setText(mModel.getStartTime());
        }
        if (mModel.getDuration().equals("")) {
            holder.textDuration.setVisibility(View.GONE);
            holder.textDurationHead.setVisibility(View.GONE);
        } else
            holder.textDuration.setText(mModel.getDuration());
        if (mModel.getEndTime().equals("")) {
            holder.textEndTime.setVisibility(View.GONE);
            holder.textEndTimeHead.setVisibility(View.GONE);
        } else
            holder.textEndTime.setText(mModel.getEndTime());

        holder.imageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(">>>>>>>>>>>>>>>" + mList.get(position).getLink());
                customTabLinking(mContext, mList.get(position).getLink());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(mList.get(position).getLink()));
//                Log.i("URL2",mList.get(position).getLink());
//                view.getContext().startActivity(intent);
            }
        });
        holder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, mList.get(position).getLink());
                mContext.startActivity(Intent.createChooser(intent, "Share Contest URL"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void customTabLinking(Context context, String url) {
        customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                //pre-warning means to fast the surfing
                mClient = customTabsClient;
                mClient.warmup(0L);
                customTabsSession = mClient.newSession(null);
            }


            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mClient = null;
            }
        };
        CustomTabsClient.bindCustomTabsService((context), "com.android.chrome", customTabsServiceConnection);
        Uri uri = Uri.parse(url);

        //Create an Intent Builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        //Set Start and Exit Animations
        intentBuilder.setStartAnimations(context, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        //build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intentBuilder.setShowTitle(true);
        intentBuilder.enableUrlBarHiding();

        customTabsIntent.launchUrl(context, uri);

    }
}

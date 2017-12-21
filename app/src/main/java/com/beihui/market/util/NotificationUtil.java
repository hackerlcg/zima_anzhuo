package com.beihui.market.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.beihui.market.R;
import com.beihui.market.ui.activity.MainActivity;

import java.util.Random;

public class NotificationUtil {
    public static void showNotification(Context context, String title, String content, Intent contentIntent, String group) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.push))
                //设置群组
                .setGroup(group)
                .setSmallIcon(R.drawable.push_small)
                //通知首次出现在通知栏，带上升动画效果的
                .setTicker(title)
                //设置通知的标题
                .setContentTitle(title)
                //设置通知的内容
                .setContentText(content)
                //通知产生的时间，会在通知信息里显示
                .setWhen(System.currentTimeMillis())
                //设置这个标志当用户单击面板就可以让通知将自动取消
                .setAutoCancel(true)
                //设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setOngoing(false)
                //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        //如果有跳转
        if (contentIntent != null) {
            builder.setContentIntent(PendingIntent.getActivity(context, 1, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //发起通知
        Random random = new Random(System.currentTimeMillis());
        notificationManager.notify(random.nextInt(), builder.build());

    }

    /**
     * 显示一个下载带进度条的通知
     */
    public static NotificationCompat.Builder showNotificationProgress(Context context) {
        //进度条通知
        final NotificationCompat.Builder builderProgress = new NotificationCompat.Builder(context);
        builderProgress.setContentTitle("下载中");
        builderProgress.setSmallIcon(R.drawable.push_small);
        builderProgress.setTicker("下载中");
        builderProgress.setProgress(100, 0, false);
        Notification notification = builderProgress.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, notification);

        return builderProgress;
    }

    public static void updateProgress(Context context, NotificationCompat.Builder builder, int progress) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setProgress(100, progress, false);
        notificationManager.notify(2, builder.build());
    }

    public static void dismissProgress(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
    }

    public static void showDownloadApkFailed(Context context) {
        final NotificationCompat.Builder builderProgress = new NotificationCompat.Builder(context);
        builderProgress.setContentTitle("安装包下载失败");
        builderProgress.setSmallIcon(R.drawable.push_small);
        builderProgress.setTicker("安装包下载失败");
        Notification notification = builderProgress.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, notification);
    }

    /**
     * 悬挂式，支持6.0以上系统
     */
    public static void showFullScreen(Context context, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle(content);
        //设置点击跳转
        Intent hangIntent = new Intent();
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        hangIntent.setClass(context, MainActivity.class);
        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
        PendingIntent hangPendingIntent = PendingIntent.getActivity(context, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setFullScreenIntent(hangPendingIntent, true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, builder.build());
    }

    /**
     * 折叠式
     */
    public static void showNotify(Context context) {
        //先设定RemoteViews
        RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.view_custom);
        //设置对应IMAGEVIEW的ID的资源图片
        view_custom.setImageViewResource(R.id.custom_icon, R.mipmap.ic_launcher);
        view_custom.setTextViewText(R.id.tv_custom_title, "");
        view_custom.setTextColor(R.id.tv_custom_title, Color.BLACK);
        view_custom.setTextViewText(R.id.tv_custom_content, "");
        view_custom.setTextColor(R.id.tv_custom_content, Color.BLACK);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContent(view_custom)
                .setContentIntent(PendingIntent.getActivity(context, 4, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("")
                .setPriority(Notification.PRIORITY_HIGH)// 设置该通知优先级
                .setOngoing(false)//不是正在进行的   true为正在进行  效果和.flag一样
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notify = mBuilder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(4, notify);
    }
}

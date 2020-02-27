package com.chuyendoidauso.chuyendoidanhba.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.support.v4.app.NotificationCompat;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.api.AppApi;
import com.chuyendoidauso.chuyendoidanhba.api.BaseObserver;
import com.chuyendoidauso.chuyendoidanhba.ui.splash.SplashActivity;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import okhttp3.ResponseBody;

public class MessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.

        System.out.println("DKM::::::");
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(remoteMessage.getData());
            NotifyData notifyData = gson.fromJson(jsonElement, NotifyData.class);
//            System.out.println("notifyData.type" + notifyData.pushType);
            if (notifyData == null) return;
            String type = "statistical";
            if (type.equals(notifyData.pushType)) {
                updateTimePushNotify(notifyData.pushId);
            } else {
                sendNotifyCustom(notifyData);
            }
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void updateTimePushNotify(String idPush) {
        // TODO: Implement this method to send token to your app server.
        AppApi.getInstance().updateTimePushNotify(idPush, new BaseObserver<ResponseBody>() {

            @Override
            protected void onResponse(final ResponseBody ads) {

            }

            @Override
            protected void onFailure() {

            }
        });
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        AppApi.getInstance().registerToken(token, new BaseObserver<ResponseBody>() {

            @Override
            protected void onResponse(final ResponseBody ads) {

            }

            @Override
            protected void onFailure() {

            }
        });
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @message body received.
     */
    private void sendNotification(NotifyData notifyData) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(TextUtils.isEmpty(notifyData.title) ?
                                getString(R.string.app_name) : notifyData.title)//
                        .setContentText(notifyData.message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifyCustom(NotifyData notifyData) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent launchIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(launchIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.layout_notificartion);
        contentView.setImageViewBitmap(R.id.notification_item_image, getBitmapIcon(this));
        contentView.setTextViewText(R.id.notification_item_text, notifyData.message);
        contentView.setTextViewText(R.id.notification_item_title, notifyData.title);
        builder.setContent(contentView);
        builder.setPriority(Notification.PRIORITY_MAX);


        int notificationId = 1;
        //define a notification manager
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    private Bitmap getBitmapIcon(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        int width = getSizeImage(context);
        return Bitmap.createScaledBitmap(bitmap, width, width, true);
    }

    private int getSizeImage(Context context) {
        return (int) context.getResources().getDimension(R.dimen.image_size);
    }


}
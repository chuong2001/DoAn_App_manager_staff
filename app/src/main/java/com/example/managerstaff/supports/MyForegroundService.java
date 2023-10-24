package com.example.managerstaff.supports;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.managerstaff.R;

public class MyForegroundService extends Service {
    // Constants for notification channel
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Thực hiện công việc của bạn ở đây

        // Tạo và hiển thị thông báo Foreground
        createNotificationChannel();
        Notification notification = createNotification();
        startForeground(1, notification);

        // Để service không bị dừng khi hoàn thành công việc, bạn cần trả về START_STICKY
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service Example")
                .setContentText("Dịch vụ đang chạy...")
                .setSmallIcon(R.drawable.icon_notify);

        return builder.build();
    }
}

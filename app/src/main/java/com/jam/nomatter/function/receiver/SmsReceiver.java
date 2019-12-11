package com.jam.nomatter.function.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.jam.nomatter.common.constant.SmsKeywordConstant;

import java.util.ArrayList;
import java.util.List;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        StringBuilder content = new StringBuilder();
        String sender = null;
        Bundle bundle = intent.getExtras();
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object object : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object, format);
                sender = message.getOriginatingAddress();
                content.append(message.getMessageBody());
                String contentStr = content.toString();
                if (contentStr.length() > 0) {
                    if (contentStr.contains(SmsKeywordConstant.XMATTER) && contentStr.contains(SmsKeywordConstant.REPLY)) {
                        int start = contentStr.indexOf(SmsKeywordConstant.REPLY);
                        int end = contentStr.indexOf(SmsKeywordConstant.ACKNOWLEDGE);
                        String substring = contentStr.substring(start + 6, end - 5);
                        Toast.makeText(context, sender + "--" + substring, Toast.LENGTH_SHORT).show();
                        sendSMS(sender, substring);
                    }
                }
            }
        }
    }

    private void sendSMS(String phone, String content) {
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phone, null, content, null, null);
    }


}

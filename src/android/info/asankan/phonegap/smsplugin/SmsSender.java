package info.asankan.phonegap.smsplugin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Asanka on 12/16/13.
 */
public class SmsSender extends Activity {
    //private Activity activity;

    //public SmsSender(Activity activity){
    //    this.activity=activity;
    //}

    public void invokeSMSIntent(String phoneNumber, String message) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", message);
        sendIntent.setType("vnd.android-dir/mms-sms");
//        activity.startActivity(sendIntent);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void sendSMS(String phoneNumber, String message) {
        /*SmsManager manager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getActivity(activity, 0, new Intent(), 0);
        PendingIntent deliveryIntent=PendingIntent.getActivity(activity,0,new Intent(),0);
        manager.sendTextMessage(phoneNumber, null, message, sentIntent, null);*/
    	String  SENT = "SMS_SENT";
        String  DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(SmsSender.this, 0,
            new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(SmsSender.this, 0,
            new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context  arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(SmsSender.this, "SMS sent", 
                                Toast.LENGTH_SHORT).show();
//                        SmsSender.this.setResult(RESULT_OK, getIntent());
//                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(SmsSender.this, "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        SmsSender.this.setResult(RESULT_CANCELED, getIntent());
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(SmsSender.this, "No service", 
                                Toast.LENGTH_SHORT).show();
                        SmsSender.this.setResult(RESULT_CANCELED, getIntent());
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(SmsSender.this, "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        SmsSender.this.setResult(RESULT_CANCELED, getIntent());
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(SmsSender.this, "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        SmsSender.this.setResult(RESULT_CANCELED, getIntent());
                        finish();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        SmsSender.this.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context  arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(SmsSender.this, "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        SmsSender.this.setResult(RESULT_OK, getIntent());
                        finish();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(SmsSender.this, "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        SmsSender.this.setResult(RESULT_CANCELED, getIntent());
                        finish();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); 
    }
}

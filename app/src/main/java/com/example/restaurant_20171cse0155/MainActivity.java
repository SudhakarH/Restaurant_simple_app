package com.example.restaurant_20171cse0155;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;



import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    Button btNotify;
    EditText text;
    EditText num;
    int quantity = 1;
    int in;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.name_field);
        num = (EditText)findViewById(R.id.num_field);
        btNotify = findViewById(R.id.notify);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int in1 = prefs.getInt("in",0);
        num.setText(in1);

        String st = prefs.getString("name","");
        text.setText(st);

    }
    public void increments(View view) {
        if(quantity == 100) {
            Toast.makeText(this, "You Cant go more that 100 Cups at once.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity+1;
        display(quantity);
    }
    public void decrements(View view) {
        if(quantity == 1) {
            Toast.makeText(this, "You Cant go less that 1 Cup.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity-1;
        display(quantity);
    }
    public void submitOrder(View view) {

        String value = text.getText().toString();

        CheckBox DosaCheckBox = (CheckBox) findViewById(R.id.dosa_checkbox);
        boolean hasDosa = DosaCheckBox.isChecked();

        CheckBox IdliCheckBox = (CheckBox) findViewById(R.id.idli_checkbox);
        boolean hasIdli = IdliCheckBox.isChecked();


        int price = calculatePrice(hasDosa,hasIdli);
        String orderSumary = createOrderSummary(price,value,hasDosa,hasIdli);

        in = Integer.parseInt(num.getText().toString());
        name = text.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("in",in);
        editor.putString("name",name);
        editor.apply();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                MainActivity.this
        )
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("Notification")
                .setContentText("order booked")
                .setAutoCancel(true);


        Intent intent = new Intent(MainActivity.this,Summary.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("summary",orderSumary);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE
        );
        notificationManager.notify(0,builder.build());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    private String createOrderSummary(int price,String name, boolean addDosa, boolean addIdli) {
        String priceMessage = "Name: " + name;
        if(addDosa) {
            priceMessage += "\nMasala Dosa";
        }
        if(addIdli) {
            priceMessage += "\nIdli Voda";
        }

        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: Rs. " + price +"\n Your Order will be delivered at 12:00 AM"+ "\nThank You!";
        return priceMessage;

    }
    private int calculatePrice(boolean addDosa, boolean addIdli) {
        int basePrice = 0;
        if(addDosa) {
            basePrice += 40;
        }
        if(addIdli) {
            basePrice += 20;
        }
        return (quantity * basePrice);
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}

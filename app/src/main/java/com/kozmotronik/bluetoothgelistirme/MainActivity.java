package com.kozmotronik.bluetoothgelistirme;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bltAdaptor;
    private Button btAcKapat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAcKapat = findViewById(R.id.btn_ac_kapat);
        bltAdaptor = BluetoothAdapter.getDefaultAdapter();
        if (bltAdaptor == null) {
            // Bu cihazda etkin bir bluetooth donanımı bulunmuyor
        }

        // Kontrol tuşumuza basınca ne yapacağını belirtiyoruz.
        btAcKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAcKapat();
            }
        });

        // Bluetooth durum değiştirdiğinde sistem bildirimini yakalalamak için gerekli kod
        IntentFilter BTDurumIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothDurumAbonesi, BTDurumIntentFilter);
    }

    private void bluetoothAcKapat() {
        if (!bltAdaptor.isEnabled()) {
            // Bluetooth etkisizleştirilmiş, etkinleştirme isteği çalıştır.
            Intent BTAcIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(BTAcIntent);
        } else {
            // Bluetooth etkinleştirilmiş, etkisizleştirme doğrudan yapılabilir.
            bltAdaptor.disable();
        }
    }

    private BroadcastReceiver bluetoothDurumAbonesi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String eylem = intent.getAction(); // Bildirim eylemini intent nesnesinden al
            if (eylem.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                /* Bluetooth durum değişimini burada yakaladık. Şimdi durum bilgisini alacağız...
                 Durum bilgisi BluetoothAdapter sınıfı içinde tanımlanmış bir int türünde değerden ibarettir.
                  Bu değer işletim sistemi tarafından intent nesnesine; BluetoothAdapter.EXTRA_STATE
                  anahtarı ile extra veri olarak koyulur. */
                int durum = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (durum){

                    case BluetoothAdapter.STATE_OFF:
                        Log.i("bluetoothDurumAbonesi","Bluetooth kapalı");
                        // Bluetooth durumunu öğrendiğimiz için burada kontrol tuşumuzun yazısını güncelleyebiliriz
                        btAcKapat.setText("BLUETOOTH AÇ");
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.i("bluetoothDurumAbonesi","Bluetooth kapanıyor...");
                        break;

                    case BluetoothAdapter.STATE_ON:
                        Log.i("bluetoothDurumAbonesi","Bluetooth açık");
                        // Burada artık bluetooth'un açık olduğunu biliyoruz. Dolayısıyla tuşumuzun yazısını güncelleyebiliriz
                        btAcKapat.setText("BLUETOOTH KAPAT");
                        break;

                    case  BluetoothAdapter.STATE_TURNING_ON:
                        Log.i("bluetoothDurumAbonesi","Bluetooth açılıyor...");
                        break;

                    case BluetoothAdapter.ERROR:
                        Log.i("bluetoothDurumAbonesi","Bluetooth ile ilgili bir hata!");
                        break;

                    default:
                        Log.i("bluetoothDurumAbonesi","Tanınmayan bir bluetooth durumu...");

                }
            }
        }
    };
}
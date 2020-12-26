package com.example.remotectrl;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends Activity{
    private SensorManager sm;
    private Sensor aSensor; //加速度感測器
    private Sensor mSensor; //磁場感測器
    float[] accelerometerValues = new float[3]; //儲存加速度感測器之數值的陣列
    float[] magneticFieldValues = new float[3]; //儲存磁場感測器之數值的陣列
    private RequestQueue mQueue;
    private TextView msg;
    private String mUrl ; // PHP 網站伺服器網址
    private String address; //伺服器 IP
    private Button send; //送出使用者輸入 IP 的按鍵
    private Button STOP; //停止割草機
    private EditText ip; //儲存使用者輸入 IP 字串

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        msg = (TextView) findViewById(R.id.msg);
        send = (Button) findViewById(R.id.get_msg);
        STOP=(Button) findViewById(R.id.stop);
        STOP.setBackgroundColor(Color.RED);
        STOP.setTextColor(Color.WHITE);

        ip=(EditText) findViewById(R.id.edit);
        mQueue = Volley.newRequestQueue(this);
        /*送出 IP 的事件*/
        send.setOnClickListener(
          new View.OnClickListener(){
          @Override
          public void onClick(View v) {
            address =ip.getText().toString().trim(); //儲存使用者輸入之 IP
            sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(myListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
            calculateOrientation(); //感測姿態以控制載具之方法
          }
        });
        /*使用者按下緊急停止鈕之事件*/
        STOP.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String para="/webserv.php?cnct=1&cmd=9";//代表停止之網址，夾帶字串 9 送至樹莓派
                mUrl="http://"+ address +para; //送出的網址
                Connect connectToServer=new Connect(mUrl,mQueue,msg);
                connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
            } });
    }

    public void onPause(){
        sm.unregisterListener(myListener);
        super.onPause();
    }

    final SensorEventListener myListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent sensorEvent) {
         if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magneticFieldValues = sensorEvent.values;
         if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelerometerValues = sensorEvent.values;
         calculateOrientation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private void calculateOrientation() { //感測姿態遙控載具之方法 calculateOrientation()
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R,null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        /*將感測到的值轉換為度*/
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        //感測到手機左傾一點之事件
        if(values[1] <=-15 && values[1] > -35 && values[2]<15&& values[2]>-15){
            String para="/webserv.php?cnct=1&cmd=3"; //代表左慢之網址，夾帶字串 3 送至樹莓派
            mUrl="http://"+address+para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        }
        //感測到手機左傾傾角大之事件
        else if(values[1] <= -35 && values[1] > -90&& values[2]<15&& values[2]>-15){
            String para="/webserv.php?cnct=1&cmd=7"; //代表左快之網址，夾帶字串 7 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg); connectToServer.connect();//連線至樹莓派，送出網址至 php 程式

        } //感測到手機右傾傾角小之事件
        else if(values[1] >= 15 && values[1] <=35&& values[2]<15&& values[2]>-15){
            String para="/webserv.php?cnct=1&cmd=4";//代表右慢之網址，夾帶字串 4 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        } //感測到手機右傾傾角大之事件
        else if(values[1] >= 35 && values[1] <=90&& values[2]<10&& values[2]>-10){
            String para="/webserv.php?cnct=1&cmd=8";//代表右快之網址，夾帶字串 8 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        } //感測到手機後傾傾角小之事件
        else if((values[2] >= 15 && values[2] < 35) ){
            String para="/webserv.php?cnct=1&cmd=2";//代表後慢之網址，夾帶字串 2 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        }//感測到手機後傾傾角大之事件
        else if(values[2] >= 35 && values[2] <=90){
            String para="/webserv.php?cnct=1&cmd=6";//代表後快之網址，夾帶字串 6 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        }//感測到手機前傾傾角小之事件
        else if(values[2] <= -15 && values[2] > -35){
            String para="/webserv.php?cnct=1&cmd=1";//代表前慢之網址，夾帶字串 1 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        }//感測到手機前傾傾角大之事件
        else if(values[2] <= -35 && values[2] >=-90){
            String para="/webserv.php?cnct=1&cmd=5"; //代表前快之網址，夾帶字串 5 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect(); //連線至樹莓派，送出網址至 php 程式
        } //感測到手機平躺之事件
        else {
            String para="/webserv.php?cnct=1&cmd=0";//代表停止之網址，夾帶字串 0 送至樹莓派
            mUrl="http://"+ address +para; //送出的網址
            Connect connectToServer=new Connect(mUrl,mQueue,msg);
            connectToServer.connect();//連線至樹莓派，送出網址至 php 程式
        }
    }

}

class Connect{  //類別 Connect，實現連接至伺服器的細節
       private RequestQueue mQueue;
       private String mUrl ;
       private StringRequest getRequest;
       private TextView msg;
       public Connect (String mUrl,RequestQueue mQueue,TextView msg) {

           this.mUrl=mUrl;
           this.mQueue=mQueue;
           this.msg=msg;
       }

       public void connect() {
           getRequest = new StringRequest(mUrl,
                   new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s){
                                msg.setText(s);
                        }
                      },
                   new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            msg.setText(volleyError.getMessage());
                        }
                    });
           mQueue.add(getRequest);
       }
}
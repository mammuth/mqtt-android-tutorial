package com.frost.mqtttutorial;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.PressureChart;
import helpers.MqttHelper;

public class MainActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    PressureChart pressureChart;
    LineChart pressureChartView;

    ImageView oarOne, oarTwo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oarOne = (ImageView) findViewById(R.id.oarOne);
        oarTwo = (ImageView) findViewById(R.id.oarTwo);

        pressureChartView = (LineChart) findViewById(R.id.chart);
        pressureChart = new PressureChart(pressureChartView);

        startMqtt();
    }

    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug","Connected");
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("MQTTMessageArrived","Topic: " + topic + ", Message:" +
                        " " + mqttMessage.toString());
                switch (topic) {
                    case "sensor/flex/one":
                        pressureChart.addEntry(Float.valueOf(mqttMessage.toString()), "Pressure1");
                        break;
                    case "sensor/flex":
                        pressureChart.addEntry(Float.valueOf(mqttMessage.toString()), "Pressure2");
                        break;
                    case "sensor/rowlock/one/degree":
                        Log.w("MQTTMessageArived", "Rotating Oar One");
                        rotateImage(oarOne, Float.valueOf(mqttMessage.toString()), 5, oarOne.getHeight());  // ToDo: Determine Pivots
                        break;
                    case "sensor/rowlock/two/degree":
                        rotateImage(oarTwo, Float.valueOf(mqttMessage.toString()), 5, oarTwo.getHeight());  // ToDo: Determine Pivots
                        break;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void rotateImage(ImageView view, float angle, float pivotX, float pivotY) {
        Matrix matrix = new Matrix();
        view.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate((float) angle, pivotX, pivotY);
        view.setImageMatrix(matrix);
    }
}

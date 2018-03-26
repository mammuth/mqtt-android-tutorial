package com.frost.mqtttutorial;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.PressureChart;
import helpers.MqttHelper;
import helpers.SeatChart;

public class MainActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    PressureChart pressureChart;
    SeatChart seatChart;
    LineChart pressureChartView, seatChartView;

    ImageView oarTopOne, oarTopTwo, oarBottomOne, oarBottomTwo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oarTopOne = (ImageView) findViewById(R.id.oarTopOne);
        oarTopTwo = (ImageView) findViewById(R.id.oarTopTwo);
        oarBottomOne = (ImageView) findViewById(R.id.oarBottomOne);
        oarBottomTwo = (ImageView) findViewById(R.id.oarBottomTwo);

        pressureChartView = (LineChart) findViewById(R.id.pressureChart);
        pressureChart = new PressureChart(pressureChartView);

        seatChartView = (LineChart) findViewById(R.id.seatChart);
        seatChart = new SeatChart(seatChartView);

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
                    case "sensor/flex/two":
                        pressureChart.addEntry(Float.valueOf(mqttMessage.toString()), "Pressure2");
                        break;
                    case "sensor/rowlock/one/degree":
                        Log.w("MQTTMessageArived", "Rotating Oar One");
                        rotateImage(oarTopOne, 180 - Float.valueOf(mqttMessage.toString()) - 90, 13, 270);
                        rotateImage(oarBottomOne, Float.valueOf(mqttMessage.toString()) - 90, 13, 130);
                        break;
                    case "sensor/rowlock/two/degree":
                        rotateImage(oarTopTwo, 180 - Float.valueOf(mqttMessage.toString()) - 90, 13, 270);
                        rotateImage(oarBottomTwo, Float.valueOf(mqttMessage.toString()) - 90, 13, 130);
                        break;
                    case "sensor/seat/one":
                        seatChart.addEntry(Float.valueOf(mqttMessage.toString()), "Seat1");
                        break;
                    case "sensor/seat/two":
                        seatChart.addEntry(Float.valueOf(mqttMessage.toString()), "Seat2");
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
        matrix.postRotate(angle, pivotX, pivotY);
        view.setScaleType(ImageView.ScaleType.MATRIX);
        view.setImageMatrix(matrix);
    }
}

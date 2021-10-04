package it.unibo.scl.es.student;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private TextView tv_xValue;
    private TextView tv_yValue;
    private TextView tv_zValue;
    private TextView tv_onTableValue;
    private RelativeLayout rl_onTableRecognition;
    private TextView tv_actRecognitionValue;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Classifier classifier;
    private final Sample sample = new Sample(0.0f, 0.0f, 0.0f);
    private long currentUpdate = 0;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        setAccelerometerValues(x, y, z);
        setOnTableRecognitionResult((z >= 9.6f && z <= 10.4f) && (Math.abs(x) + Math.abs(y)) <= 0.5f);

        currentUpdate = System.currentTimeMillis();
        if ((currentUpdate - sample.getLastUpdate()) > 100) {
            long diffTime = (currentUpdate - sample.getLastUpdate());
            sample.setLastUpdate(currentUpdate);

            float speed = Math.abs(x + y + z - sample.getX() - sample.getY() - sample.getZ()) / diffTime * 10000;

            int SHAKE_THRESHOLD = 2000;
            if (speed > SHAKE_THRESHOLD) {
                shake();
            }

            sample.setX(x);
            sample.setY(y);
            sample.setZ(z);

            String res = classifier.classify(x, y, z);
            if (!res.isEmpty()) {
                setActivityRecognitionResult(res);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_xValue = (TextView) findViewById(R.id.tv_x_value);
        tv_yValue = (TextView) findViewById(R.id.tv_y_value);
        tv_zValue = (TextView) findViewById(R.id.tv_z_value);
        tv_onTableValue = (TextView) findViewById(R.id.tv_on_table_recognition_value);
        rl_onTableRecognition = (RelativeLayout) findViewById(R.id.rl_on_table_recognition);
        tv_actRecognitionValue = (TextView) findViewById(R.id.tv_act_rec_value);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        classifier = new Classifier();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        //rimozione del listener del sensore
        super.onPause();
    }

    //callback onClick del button Start
    public void start(View view) {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //callback onClick del button Stop
    public void stop(View view) {
        sensorManager.unregisterListener(this, sensor);
    }

    public void setAccelerometerValues(float x, float y, float z) {
        tv_xValue.setText(x + "");
        tv_yValue.setText(y + "");
        tv_zValue.setText(z + "");
    }

    public void setOnTableRecognitionResult(boolean result) {
        if (result) {
            tv_onTableValue.setText("On Table");
            rl_onTableRecognition.setBackgroundResource(R.drawable.ontable);
        } else {
            tv_onTableValue.setText("Not on Table");
            rl_onTableRecognition.setBackgroundResource(R.drawable.border);
        }
    }

    public void shake() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        findViewById(R.id.tv_shake_value).startAnimation(shake);
    }

    public void setActivityRecognitionResult(String result) {
        tv_actRecognitionValue.setText(result);
    }

}

class Sample {

    private float x;
    private float y;
    private float z;
    private long lastUpdate;

    public Sample(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.lastUpdate = 0;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}


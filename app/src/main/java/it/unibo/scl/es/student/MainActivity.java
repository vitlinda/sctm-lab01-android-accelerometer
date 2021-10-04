package it.unibo.scl.es.student;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView tv_xValue;
	private TextView tv_yValue;
	private TextView tv_zValue;
	private TextView tv_onTableValue;
	private RelativeLayout rl_onTableRecognition;
	private TextView tv_actRecognitionValue;
	
	//dichiarare campi necessari


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_xValue = (TextView)findViewById(R.id.tv_x_value);
		tv_yValue = (TextView)findViewById(R.id.tv_y_value);
		tv_zValue = (TextView)findViewById(R.id.tv_z_value);
		tv_onTableValue = (TextView)findViewById(R.id.tv_on_table_recognition_value);
		rl_onTableRecognition = (RelativeLayout)findViewById(R.id.rl_on_table_recognition);
		tv_actRecognitionValue = (TextView)findViewById(R.id.tv_act_rec_value);

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

	//implementare opportuni metodi di interfaccia

	//callback onClick del button Start
	public void start(View view) {
		//registrare listener del sensore
	}
	
	//callback onClick del button Stop
	public void stop(View view) {
		//rimozione del listener del sensore
	}

	
	public void setAccelerometerValues(float x, float y, float z){
		tv_xValue.setText(x+"");
		tv_yValue.setText(y+"");
		tv_zValue.setText(z+"");
	}
	
	public void setOnTableRecognitionResult(boolean result){
		if(result){
			tv_onTableValue.setText("On Table");
			rl_onTableRecognition.setBackgroundResource(R.drawable.ontable);
		}else{
			tv_onTableValue.setText("Not on Table");
			rl_onTableRecognition.setBackgroundResource(R.drawable.border);
		}
	}
	
	public void shake() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
		findViewById(R.id.tv_shake_value).startAnimation(shake);
	}
	
	public void setActivityRecognitionResult(String result){
		tv_actRecognitionValue.setText(result);
	}

}

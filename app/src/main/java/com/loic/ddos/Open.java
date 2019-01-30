package com.loic.ddos;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.View;
import java.util.TimerTask;
import java.util.Timer;
import com.loic.ddos.text.TypeWriterView;
import com.loic.ddos.text.TypeWriterListener;

public class Open extends Activity {
	private Timer _t = new Timer();
	private int TranslationY;
	private ImageView img;
	private ObjectAnimator oa1 = new ObjectAnimator();
	private ObjectAnimator oa2 = new ObjectAnimator();
	private TypeWriterView title;
	private TypeWriterView msg;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open);
		img = (ImageView)findViewById(R.id.img);
		title = (TypeWriterView)findViewById(R.id.title);
		msg = (TypeWriterView)findViewById(R.id.msg);
		oa1.setTarget(img);
		oa1.setPropertyName("alpha");
		oa1.setFloatValues((float)(1.0d));
		oa1.setFloatValues((float)(0.0d), (float)(1.0d));
		oa1.setDuration(2000);
		oa1.setInterpolator(new LinearInterpolator());
		oa1.start();
		oa1.addListener(new Animator.AnimatorListener() {
			public void onAnimationStart(Animator _p){}
			public void onAnimationCancel(Animator _p){}
			public void onAnimationRepeat(Animator _p){}
			public void onAnimationEnd(Animator _p){onAnimation();}
		});
	}
	public void onAnimation() {
		TranslationY = getHeight() / 2 - getHeight() / 8;
		oa2.setTarget(img);
		oa2.setPropertyName("translationY");
		oa2.setFloatValues(-TranslationY);
		oa2.setFloatValues((float)(0.0d), (float)(-TranslationY));
		oa2.setDuration(1500);
		oa2.setInterpolator(new LinearInterpolator());
		oa2.start();
		oa2.addListener(new Animator.AnimatorListener() {
			public void onAnimationStart(Animator _p){}
			public void onAnimationCancel(Animator _p){}
			public void onAnimationRepeat(Animator _p){}
			public void onAnimationEnd(Animator _p){_onAnimation();}
		});
	}
	public void _onAnimation() {
		title.setDelay(100);
		title.setWithMusic(false, 0);
		title.animateText("Warnink!!!");
		title.setTypeWriterListener(new TypeWriterListener() {
			public void onTypingStart(String _txt) {}
			public void onCharacterTyped(String _txt, int _pos){}
			public void onTypingRemoved(String _txt){}
			public void onTypingEnd(String _txt){_onAnimation2();img.setVisibility(View.GONE);}
		});
	}
	public void _onAnimation2() {
		msg.setDelay(100);
		msg.setWithMusic(false, 0);
		msg.animateText("This Are Tools For Hacker\nWe Are Anonymous\nWe Are Legion\nWe don't forgive\nWe don't Forget\nEXPECT US");
		msg.setTypeWriterListener(new TypeWriterListener() {
			public void onTypingStart(String _txt) {}
			public void onCharacterTyped(String _txt, int _pos){}
			public void onTypingRemoved(String _txt){}
			public void onTypingEnd(String _txt){
				Intent i = new Intent(Open.this, Main.class);
				try {
					startActivity(i);
					finish();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	public int getHeight() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}

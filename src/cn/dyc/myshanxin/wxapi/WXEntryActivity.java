package cn.dyc.myshanxin.wxapi;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.dyc.myshanxin.R;
import cn.dyc.myshanxin.client.view.AppUtil;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;

public class WXEntryActivity extends Activity implements OnClickListener,
		IWXAPIEventHandler {
	private Button addPic, shareBtn;
	private ImageView shareImg;
	

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private final int CAMERA_CODE = 1; // 这里的IMAGE_CODE是自己任意定义的
	private String path = "";
	private static final int THUMB_SIZE = 150;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	// 已通过审核APP_ID
	public final String APP_ID = "wx2857466cea9caf02";
	private Bitmap mBitmap = null;
	boolean isPicture = false;
	private String filePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		// 将该app注册到微信
		api.registerApp(APP_ID);
		api.handleIntent(getIntent(), this);

		shareImg = (ImageView) findViewById(R.id.shareImg);
		addPic = (Button) findViewById(R.id.addPic);
		shareBtn = (Button) findViewById(R.id.shareBtn);
		addPic.setOnClickListener(this);
		shareBtn.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addPic:
			new AlertDialog.Builder(this)
					.setTitle("选择")
					.setItems(new String[] { "拍照", "本地相册", "取消" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										/** 打开相机 */
										Intent intent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										startActivityForResult(intent,
												CAMERA_CODE);

										break;
									case 1:
										// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
										Intent getAlbum = new Intent(
												Intent.ACTION_GET_CONTENT);
										getAlbum.setType(IMAGE_TYPE);
										startActivityForResult(getAlbum,
												IMAGE_CODE);

										break;
									case 2:

										break;

									default:
										break;
									}

								}
							}).show();
			break;
		case R.id.shareBtn:
			// 检查网络状态
			if (!checkNetworkInfo()) {
				return;
			}
			if (!isPicture) {
				Toast.makeText(WXEntryActivity.this, "请选择分享图片",
						Toast.LENGTH_LONG).show();
			} else {

				if (api.isWXAppInstalled()) {

					new AlertDialog.Builder(this)
							.setTitle("分享")
							.setItems(new String[] { "微信好友", "微信朋友圈", "取消" },
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											switch (which) {
											// 微信好友
											case 0:
												weixinShare(true);

												break;
											// 微信朋友圈
											case 1:
												weixinShare(false);
												break;
											case 2:

												break;

											default:
												break;
											}

										}
									}).show();

				} else {

					Toast.makeText(WXEntryActivity.this, "您还未安装微信应用",
							Toast.LENGTH_SHORT).show();

				}
			}
			break;

		default:
			break;
		}

	}

	private void weixinShare(boolean isFriend) {

		File file = new File(path);
		if (!file.exists()) {
			String tip = WXEntryActivity.this
					.getString(R.string.send_img_file_not_exist);
			Toast.makeText(WXEntryActivity.this, tip + " path = " + path,
					Toast.LENGTH_LONG).show();
			return;
		}
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(path);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap bmp = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
				THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

		int imageSize = msg.thumbData.length / 1024;
		if (imageSize > 32) {
			Toast.makeText(WXEntryActivity.this, "您分享的图片过大", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = isFriend ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
		finish();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Toast.makeText(WXEntryActivity.this, "操作取消", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		switch (requestCode) {
		case IMAGE_CODE:
			Bitmap bm = null;
			// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
			ContentResolver resolver = getContentResolver();
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片

				// 这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaColumns.DATA };

				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);

				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);

				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();

				// 最后根据索引值获取图片路径
				path = cursor.getString(column_index);
				Bitmap bitmap = AppUtil.getLoacalBitmap(path); // 从本地取图片(在cdcard中获取)
				shareImg.setImageBitmap(bitmap); // 设置Bitmap
				isPicture = true;
			} catch (IOException e) {
				Toast.makeText(WXEntryActivity.this, e.toString(),
						Toast.LENGTH_SHORT).show();

			}
			break;
		case CAMERA_CODE:
			if (data != null) {
				// HTC
				if (data.getData() != null) {
					// 根据返回的URI获取对应的SQLite信息
					Cursor cursor = getContentResolver().query(data.getData(),
							null, null, null, null);
					if (cursor.moveToFirst()) {
						filePath = cursor.getString(cursor
								.getColumnIndex("_data"));// 获取绝对路径
					}
					cursor.close();
				} else {
					// 三星 小米手机不会自动存储DCIM
					mBitmap = (Bitmap) (data.getExtras() == null ? null : data
							.getExtras().get("data"));
				}

				// 直接强转报错 这个主要是为了去高宽比例
				Bitmap bitmap = mBitmap == null ? null : (Bitmap) mBitmap;

				if (bitmap == null) {
					/**
					 * 该Bitmap是为了获取压缩后的文件比例 如果没有缩略图的比例
					 * 就获取真实文件的比例(真实图片比例会耗时很长，所以如果有缩略图最好)
					 */
					bitmap = BitmapFactory.decodeFile(filePath);
				}
				path = AppUtil.saveBitmap(bitmap);
				shareImg.setImageBitmap(AppUtil.getLoacalBitmap(path)); // 设置Bitmap
				isPicture = true;
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {

		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "分享取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送失败";
			break;
		default:
			result = "出现异常";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private static Boolean isExit = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						isExit = false;
					}
				}, 2000);
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}

	public boolean checkNetworkInfo() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接

		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return true;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return true;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("网络不给力")
				.setTitle("提示")
				.setCancelable(false)
				.setPositiveButton("配置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						// 进入无线网络配置界面
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
						WXEntryActivity.this.finish();
					}
				})
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						WXEntryActivity.this.finish();
					}
				});
		builder.show();
		return false;

	}
	
	public void btn_back(View v) {     //标题栏 返回按钮
      	this.finish();
      } 

}

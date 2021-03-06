package com.domyos.econnected.ui.fragment.user;/**
 * Created by HouWei on 16/8/11.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.biz.UserInfoBiz;
import com.domyos.econnected.biz.iml.UserBizImpl;
import com.domyos.econnected.biz.listener.UpProfileCallBack;
import com.domyos.econnected.enity.UserInfoData;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.event.UserEditEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.view.ScaleView;
import com.domyos.econnected.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author HouWei
 * Date 16/8/11
 * Time 16:09
 * Package com.yuedong.apps.treadmill.ui.fragment.user
 */
public class SetHeightFragment extends BaseFragment implements UpProfileCallBack {
	@BindView(R.id.mHeightTextView)
	TextView mMHeightTextView;
	@BindView(R.id.mScaleView)
	ScaleView mMScaleView;
	@BindView(R.id.height_back)
	ImageView height_back;


	private static final int DEFAULT_HEIGHT = 175;
	private static final int MAX_HEIGHT = 250;
	private static final int MIN_HEIGHT = 100;

	private int mHeight = DEFAULT_HEIGHT;
	private int changId = -1;
	private UserInfoBiz userInfoBiz = new UserBizImpl();
	@Override
	protected int getRootViewLayoutId() {
		return R.layout.fragment_set_height;
	}

	@Override
	protected void initSomething() {
		changId = (int) YDApplication.getInstance().get("edit");
		if(changId !=1){
			height_back.setVisibility(View.GONE);
		}
		mMHeightTextView.setText(mHeight + "");
		mMScaleView.initViewParam(mHeight, MAX_HEIGHT, MIN_HEIGHT, ScaleView.MOD_TYPE_ONE);
		mMScaleView.setDrawBottomScale(true);
		mMScaleView.setDrawTopScale(true);
		mMScaleView.setValueChangeListener(new ScaleView.OnValueChangeListener() {
			@Override
			public void onValueChange(float value) {
				mHeight = (int) value;
				mMHeightTextView.setText(mHeight + "");
			}
		});
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int height) {
		mHeight = height;
		if (mMScaleView != null) {
			mMScaleView.initViewParam(mHeight, MAX_HEIGHT, MIN_HEIGHT, ScaleView.MOD_TYPE_ONE);
		}
	}


	@OnClick({R.id.height_confirm,R.id.height_back})
	public void onClick(View view){

		switch (view.getId()){
			case R.id.height_confirm:

				upUserProfile();
				break;
			case R.id.height_back:

				if (changId == 1) {
					getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
				}
				break;

		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void getChange(UserEditEvent event) {

		switch (event.action) {
			case UserEditEvent.ACTION_EDIT_HEIGHT:
				changId = 1;
				break;
		}
	}
	private void upUserProfile() {

		SharedPreferenceUtils.put(getActivity(), "height", getHeight());

		if(changId==1){
			int picCode = SharedPreferenceUtils.get(getActivity(), "picId", 0);
			int age = SharedPreferenceUtils.get(getActivity(), "age", 0);
			int weight =  SharedPreferenceUtils.get(getActivity(), "weight", 0);
			int id = SharedPreferenceUtils.get(getActivity(), "user_id", 0);
			UserInfoData userInfoData1 = new UserInfoData();
			userInfoData1.setAge(age);
			userInfoData1.setId(id);
			userInfoData1.setHeight(getHeight());
			userInfoData1.setWeight(weight);
			userInfoData1.setPicId(picCode);
			userInfoBiz.UpProfile(userInfoData1, this);

		} else {
			removeFragment();
		}

	}

	private void removeFragment() {
		if (changId == 1) {
			EventBus.getDefault().post(new UserEditEvent(UserEditEvent.ACTION_HEIGHT_BACK));
		} else {
			EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_WEIGHT_FRAGMENT));
		}
		getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();

		// getFragmentManager().popBackStack();
	}

	@Override
	public void UpProfileSuccess(boolean profile, String msg) {
		removeFragment();
	}

	@Override
	public void UpProfileFailed(String msg) {
		if(msg.equals("用户未登录")){
			showDialog("Login Again Please");
		}
	}
	public void showDialog(String msg) {
		if (getActivity() != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.create();
			builder.setMessage(msg);
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
				}
			});
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_SHOW_LOGINFRAGMENT));
					removeFragment();
				}
			});
			builder.show();
		}

	}

}

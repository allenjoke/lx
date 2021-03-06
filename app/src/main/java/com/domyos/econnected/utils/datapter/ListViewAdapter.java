package com.domyos.econnected.utils.datapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.enity.CheckInCallBackInfo;

import java.text.DecimalFormat;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

	private Context context;
	private List<CheckInCallBackInfo.RaceCheckOutInfo> list;
	private LayoutInflater inflater;
	private int index;
	private int type;

	public ListViewAdapter(Context context, List<CheckInCallBackInfo.RaceCheckOutInfo> list, int intdex, int type) {
		this.context = context;
		this.list = list;
		this.index = intdex;
		this.type = type;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder myHolder;
		if (view == null) {
			myHolder = new ViewHolder();
			view = inflater.inflate(R.layout.race_list_item, null);
			myHolder.linearLayout = view.findViewById(R.id.race_list_bg);
			myHolder.imageView_img = view.findViewById(R.id.race_list_img);
			myHolder.imageView_tye = view.findViewById(R.id.race_list_type);
			myHolder.textView_name = view.findViewById(R.id.race_list_name);
			myHolder.textView_meters = view.findViewById(R.id.race_list_meters);

			if (type == 2) {
				if (i == index) {
					myHolder.linearLayout.setBackground(context.getDrawable(R.drawable.blue_corner));
				}
				float distance = list.get(i).getDistance();
				myHolder.textView_meters.setText(String.format("%1.2f", distance) + "km");
			}

			if (type == 3) {
				if (i == index) {
					myHolder.linearLayout.setBackground(context.getDrawable(R.drawable.blue_corner));
				}
				int time = (int)list.get(i).getTime();
				int seconds = time % 60;
				int minutes = (time / 60) % 60;
				String timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
				myHolder.textView_meters.setText(timeStr);

			}
			if (type == 1) {
				if (i == index) {
					myHolder.linearLayout.setBackground(context.getDrawable(R.drawable.blue_corner));
				}
				myHolder.textView_meters.setText(list.get(i).getCalorie()+"kcal");
			}

			if (type == 4) {

				if (i == index) {
					myHolder.linearLayout.setBackground(context.getDrawable(R.drawable.blue_corner));
				}
				if (i == 0) {
					if (i == index) {
						myHolder.textView_meters.setText("");
					} else {
						float d1 = list.get(0).getDistance() - list.get(index).getDistance();
						if (d1 > 0) {
							if (d1 >= 1) {
								myHolder.textView_meters.setText("+" + String.format("%1.2f", d1) + "km");
							} else {
								myHolder.textView_meters.setText("+" + (int) (d1 * 1000) + "m");
							}
						} else {
							float d = list.get(index).getDistance() - list.get(0).getDistance();
							if (d1 <= -1) {
								myHolder.textView_meters.setText("-" + String.format("%1.2f", d) + "km");
							} else {
								myHolder.textView_meters.setText("-" + (int) (d * 1000) + "m");
							}
						}
					}
				}
				if (i == 1) {
					if (i == index) {
						myHolder.textView_meters.setText("");
					} else {
						float d1 = list.get(1).getDistance() - list.get(index).getDistance();
						//此用户在前面
						if (d1 > 0) {
							if (d1 >= 1) {
								myHolder.textView_meters.setText("+" + String.format("%1.2f", d1) + "km");
							} else {
								myHolder.textView_meters.setText("+" + (int) (d1 * 1000) + "m");
							}
						} else {
							//此用户在后面
							float d = list.get(index).getDistance() - list.get(1).getDistance();
							if (d >= 1) {
								myHolder.textView_meters.setText("-" + String.format("%1.2f", d) + "km");
							} else {
								myHolder.textView_meters.setText("-" + (int) (d * 1000) + "m");

							}
						}

					}
				}
				if (i == 2) {

					if (i == index) {
						myHolder.textView_meters.setText("");
					} else {
						float d2 = list.get(2).getDistance() - list.get(index).getDistance();
						if (d2 > 0) {
							if (d2 >= 1) {
								myHolder.textView_meters.setText("-" + String.format("%1.2f", d2) + "km");
							} else {
								myHolder.textView_meters.setText("-" + (int) (d2 * 1000) + "m");
							}
						} else {
							float d = list.get(index).getDistance() - list.get(2).getDistance();
							if (d >= 1) {
								myHolder.textView_meters.setText("+" + String.format("%1.2f", d) + "km");
							} else {
								myHolder.textView_meters.setText("+" + (int) (d * 1000) + "m");
							}
						}
					}
				}
				if (i == 3) {
					if (i == index) {
						myHolder.textView_meters.setText("");
					} else {
						float d3 = list.get(3).getDistance() - list.get(index).getDistance();
						if (d3 > 0) {
							if (d3 >= 1) {
								myHolder.textView_meters.setText("+" + String.format("%1.2f", d3) + "km");
							} else {
								myHolder.textView_meters.setText("+" + (int) (d3 * 1000) + "m");
							}
						} else {
							float dd = list.get(index).getDistance() - list.get(3).getDistance();
							if (dd >= 1) {
								myHolder.textView_meters.setText("-" + String.format("%1.2f", dd) + "km");
							} else {
								myHolder.textView_meters.setText("-" + (int) (dd * 1000) + "m");
							}
						}
					}
				}
				if (i == 4) {
					if (i == index) {
						myHolder.textView_meters.setText("");
					} else {
						float d3 = list.get(4).getDistance() - list.get(index).getDistance();
						if (d3 > 0) {
							if (d3 >= 1) {
								myHolder.textView_meters.setText("+" + String.format("%1.1f", d3) + "km");
							} else {
								myHolder.textView_meters.setText("+" + (int) (d3 * 1000) + "m");

							}
						} else {
							float dd = list.get(index).getDistance() - list.get(4).getDistance();
							if (dd >= 1) {
								myHolder.textView_meters.setText("-" + String.format("%1.1f", dd) + "km");
							} else {
								myHolder.textView_meters.setText("-" + (int) (dd * 1000) + "m");
							}
						}
					}

				}
				if (i == 5) {
					if (i == index) {
						myHolder.textView_meters.setText("");
					} else {
						float d3 = list.get(5).getDistance() - list.get(index).getDistance();
						if (d3 > 0) {
							if (d3 >= 1) {
								myHolder.textView_meters.setText("+" + String.format("%1.1f", d3) + "km");
							} else {
								myHolder.textView_meters.setText("+" + (int) (d3 * 1000) + "m");
							}
						} else {
							float dd = list.get(index).getDistance() - list.get(5).getDistance();
							if (dd >= 1) {
								myHolder.textView_meters.setText("-" + String.format("%1.1f", dd) + "km");
							} else {
								myHolder.textView_meters.setText("-" + (int) (dd * 1000) + "m");
							}

						}
					}
				}

			}
			int picId = list.get(i).getPicId();
			setAvtar(picId, myHolder.imageView_img);
			int deviceType = list.get(i).getDeviceType();
			if (deviceType == 0) {
				myHolder.imageView_tye.setImageDrawable(context.getDrawable(R.drawable.treadmill));
			} else if (deviceType == 1) {
				myHolder.imageView_tye.setImageDrawable(context.getDrawable(R.drawable.bike));
			} else if (deviceType == 2) {
				myHolder.imageView_tye.setImageDrawable(context.getDrawable(R.drawable.icon_equipment_ep));
			}


			myHolder.textView_name.setText(list.get(i).getUsername());

			view.setTag(myHolder);

		} else {
			myHolder = (ViewHolder) view.getTag();
		}

		return view;
	}


	class ViewHolder {
		public LinearLayout linearLayout;
		public ImageView imageView_tye, imageView_img;
		public TextView textView_name, textView_meters;


	}

	public void setAvtar(int picId, ImageView imageView) {

		if (picId == 0) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.touxiang_img));

		}
		if (picId == UserPicConstant.TYPE_01_01) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_01_01));

		}

		if (picId == UserPicConstant.TYPE_01_02) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_01_02));

		}

		if (picId == UserPicConstant.TYPE_01_03) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_01_03));

		}
		if (picId == UserPicConstant.TYPE_02_01) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_02_01));

		}

		if (picId == UserPicConstant.TYPE_02_02) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_02_02));

		}

		if (picId == UserPicConstant.TYPE_02_03) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_02_03));

		}
		if (picId == UserPicConstant.TYPE_03_01) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_03_01));

		}

		if (picId == UserPicConstant.TYPE_03_02) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_03_02));

		}

		if (picId == UserPicConstant.TYPE_03_03) {
			imageView.setImageDrawable(context.getDrawable(R.drawable.pic_03_03));

		}
	}


}

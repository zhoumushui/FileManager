package com.tchip.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tchip.filemanager.R;
import com.tchip.filemanager.ViewHolder;
import com.tchip.filemanager.R.id;
import com.tchip.filemanager.R.layout;
import com.tchip.filemanager.adapter.NavDrawerAdapter.NavDrawerItem;
import com.tchip.filemanager.ui.activity.MainActivity;

import java.util.List;

public class NavDrawerAdapter extends RobotoAdapter<NavDrawerItem> {
	private static final int TYPE_COUNT = 2;

	public static interface NavDrawerItem {
		public static final int TYPE_SHORTCUT = 0, TYPE_SECTION_DIVIDER = 1;

		CharSequence getTitle(Context context);

		CharSequence getSubTitle(Context context);

		void setImageToView(ImageView imageView);

		boolean onClicked(MainActivity activity);

		int getViewType();
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	public int getItemViewType(int position) {
		return getItem(position).getViewType();
	}

	public NavDrawerAdapter(Context context, List<NavDrawerItem> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			switch (getItemViewType(position)) {
			case NavDrawerItem.TYPE_SECTION_DIVIDER:
				view = layoutInflater.inflate(
						android.R.layout.simple_list_item_1, parent, false);
				break;
			case NavDrawerItem.TYPE_SHORTCUT:
				view = layoutInflater.inflate(R.layout.file_list_item_file,
						parent, false);
				break;
			default:
				throw new RuntimeException(
						"Nav drawer item does not conform to available view types");
			}
			view.setTag(viewHolder = new ViewHolder(view));
			applyFont(view);
		} else
			viewHolder = (ViewHolder) view.getTag();

		NavDrawerItem item = getItem(position);

		switch (getItemViewType(position)) {
		case NavDrawerItem.TYPE_SHORTCUT:
			TextView tvName = viewHolder.getViewById(R.id.tvFileName),
			tvSubtitle = viewHolder.getViewById(R.id.tvFileDetails);
			ImageView imgIcon = viewHolder.getViewById(R.id.imgFileIcon);

			tvName.setText(item.getTitle(getContext()));
			tvSubtitle.setText(item.getSubTitle(getContext()));
			item.setImageToView(imgIcon);
			break;

		case NavDrawerItem.TYPE_SECTION_DIVIDER:
			TextView tvTitle = viewHolder.getViewById(android.R.id.text1);
			tvTitle.setText(item.getTitle(getContext()));
			break;
		}

		return view;
	}
}

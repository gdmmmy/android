package com.librelio.lib.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.librelio.lib.LibrelioApplication;
import com.librelio.lib.model.MagazineModel;
import com.librelio.lib.ui.DownloadActivity;
import com.niveales.wind.R;

public class MagazineAdapter extends BaseAdapter{
	private static final String TAG = "MagazineAdapter";
	private Context context;
	private ArrayList<MagazineModel> magazine;
	
	public MagazineAdapter(ArrayList<MagazineModel> magazine,Context context){
		this.context = context;
		this.magazine = magazine;
	}
	
	
	@Override
	public int getCount() {
		return magazine.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View res;
		final MagazineModel currentMagazine = magazine.get(position);
		
		if(convertView == null){
			res = LayoutInflater.from(context).inflate(R.layout.magazine_list_item, null);
		} else {
			res = convertView;
		}
		TextView title = (TextView)res.findViewById(R.id.item_title);
		TextView subtitle = (TextView)res.findViewById(R.id.item_subtitle);
		ImageView thumbnail = (ImageView)res.findViewById(R.id.item_thumbnail);

		title.setText(currentMagazine.getTitle());
		subtitle.setText(currentMagazine.getSubtitle());
		
		String imagePath = currentMagazine.getPngPath();
		thumbnail.setImageBitmap(BitmapFactory.decodeFile(imagePath));
		/**
		 * but1 - this button can be "Download button" or "Read button"
		 */
		Button but1 = (Button)res.findViewById(R.id.item_button_one);
		/**
		 * but2 - this button can be "Delete button" or "Sample button"
		 */
		Button but2 = (Button)res.findViewById(R.id.item_button_two);

		if(currentMagazine.isDownloaded()){
			//Read case
			but1.setText(context.getResources().getString(R.string.read));
			but1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LibrelioApplication.startPDFActivity(context,currentMagazine.getPdfPath());
				}
			});
		} else {
			//download case
			but1.setText(context.getResources().getString(R.string.download));
			but1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});			
		}
		//
		if((!currentMagazine.isPaid())||currentMagazine.isDownloaded()){
			//delete case
			but2.setText(context.getResources().getString(R.string.delete));
			but2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});	
		} else {
			//Sample case
			but2.setText(context.getResources().getString(R.string.sample));
			but2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					File sample = new File(currentMagazine.getSamplePath());
					Log.d(TAG, "test: "+sample.exists()+" "+currentMagazine.isSampleDownloaded());
					if(currentMagazine.isSampleDownloaded()){
						LibrelioApplication.startPDFActivity(context,currentMagazine.getSamplePath());
					} else {
						Intent intent = new Intent(context, DownloadActivity.class);
						intent.putExtra(DownloadActivity.FILE_URL_KEY, currentMagazine.getSampleUrl());
						intent.putExtra(DownloadActivity.FILE_PATH_KEY, currentMagazine.getSamplePath());
						intent.putExtra(DownloadActivity.PNG_PATH_KEY, currentMagazine.getPngPath());
						context.startActivity(intent);
					}
				}
			});
		}
		
		return res;
	}
	
}

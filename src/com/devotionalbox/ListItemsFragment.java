package com.devotionalbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.devotionalbox.VideosListAdapter.ViewHolder;
import com.devotionalbox.util.Constant;
import com.devotionalbox.util.XMLUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListItemsFragment extends Fragment {
	private ListView listItem;
	private String clickUrl ;
	private ImageView backImg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.events_xml, container,false);
		backImg = (ImageView)v.findViewById(R.id.backImage);
		backImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		clickUrl = getArguments().getString("click");
		listItem = (ListView)v.findViewById(R.id.listItems);
		new GetVideosTask(getActivity(),listItem,clickUrl).execute();
		return v;
	}

	class GetVideosTask extends AsyncTask<String, Void, String> { 
		ArrayList<VideosBean> myVideosList = new ArrayList<VideosBean>();   
		String errorMessage = "";
		Activity _activity;
		private ProgressDialog dialog;
		private ListView listItem;
		String url;
		public GetVideosTask(FragmentActivity activity, ListView listItem, String clickUrl) {
			_activity = activity; 
			this.listItem = listItem;
			url = clickUrl;
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(_activity, "", "Loading. Please wait...", true);
			dialog.show();
		} 	    	

		@Override
		protected String doInBackground(String... urls) {
			String result = "";
			if(!XMLUtil.isNetworkAvailable(_activity)) {
				result = "false";
				errorMessage = "Oops! Network problem...";
				return result;
			}
			String xml = XMLUtil.getXML(url); 
			System.out.println("xml"+xml);

			Document doc=null;
			doc = XMLUtil.XMLfromString(xml);
			if (doc == null) {
				result = "false";
				errorMessage = "Invalid XML";
				return result;
			}        
			NodeList itemList = doc.getElementsByTagName("item");

			if((itemList.getLength() <= 0)){
				result = "false";
				errorMessage = "No Videos found";
				return result;
			}

			for (int i = 0; i < itemList.getLength(); i++) {							
				VideosBean video = new VideosBean();					
				Element e1 = (Element)itemList.item(i);
				video.setTitle(XMLUtil.getValue(e1,"title"));
				video.setLink(XMLUtil.getValue(e1,"link"));
				//			video.setComments(XMLUtil.getValue(e1,"comments"));
				//			video.setDescription(XMLUtil.getValue(e1,"description"));
				String time = XMLUtil.getValue(e1,"pubDate");
				Date date=null;
				try {
					date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				long timestamp = date.getTime()/1000;  ///datetime in seconds
				video.setPubDate(XMLUtil.getTimeString(timestamp));
				myVideosList.add(video);	
			}			
			result = "true";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if(dialog != null)
				dialog.dismiss();
			if(result.equalsIgnoreCase("false")) {
				Toast.makeText(_activity, errorMessage, Toast.LENGTH_SHORT).show();
			}else {
				VideosListAdapter ListAdapter = new VideosListAdapter(_activity, myVideosList);    
				listItem.setAdapter(ListAdapter);
			}
		}
	}
}
class VideosListAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<VideosBean> videosList;
	private LayoutInflater inflater;
	public VideosListAdapter(Activity context, ArrayList<VideosBean> itemList) {
		super();
		this.context = context;
		this.videosList = itemList;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		return videosList.size();
	}

	@Override
	public Object getItem(int position) {
		return videosList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder {
		ImageView videoPic;
		TextView title;
		TextView uploadDate;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final VideosBean video = videosList.get(position);
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.videos_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.videoPic = (ImageView) convertView.findViewById(R.id.imgViewLogo);
			holder.uploadDate = (TextView) convertView.findViewById(R.id.pubDate);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}

		holder.title.setText(video.getTitle());
		holder.uploadDate.setText(video.getPubDate());

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, WebViewActivity.class);
				intent.putExtra("link", videosList.get(position).getLink());
				context.startActivity(intent);
			}
		});

		return convertView;
	}

}
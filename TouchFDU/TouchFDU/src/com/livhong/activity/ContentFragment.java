package com.livhong.activity;

import com.livhong.touchfdu.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment extends Fragment {

	View view;
	private int position;
	
	public ContentFragment(int position){
		this.position = position;
	}
	
	public View getView(){
		return view;
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        view = inflater.inflate(R.layout.content_inner_layout,container,false);
////        TextView textView = (TextView) view.findViewById(R.id.content_text);
////        textView.setText("Content : "+ this.position);
////		return view;
//		System.out.println("Created!");
//		TextView text = new TextView(container.getContext());
//        text.setText("Fragment content "+position);
//        text.setGravity(Gravity.CENTER);
//
//        return text;
//	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView text = new TextView(container.getContext());
        text.setText("Fragment content "+position);
        text.setGravity(Gravity.CENTER);

        return text;
    }
	
}

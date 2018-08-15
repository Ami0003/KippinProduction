package com.kippin.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MyKeyboard {

	private boolean threadStarted = false;
	private boolean isProcessing = false;
	private View root; 
	private KeyboardListener keyboardListener;
	private EditText input = null;
	private InputMethodManager inputMethodManager ;
	private Activity activity;
	
	
	public MyKeyboard(Activity context ,View root,KeyboardListener  listener) {
		this.activity = context;
		this.root = root;
		inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		this.keyboardListener = listener;
	}
	
	public void addGlobalCallbackListener(){
		if(!threadStarted){
			threadStarted = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					root.getViewTreeObserver().addOnGlobalLayoutListener(global);
					threadStarted = false;
				}
			}).start();		
		}
	}
	
	
	private OnGlobalLayoutListener global=new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			if(!isProcessing){

				isProcessing = true;
				 int heightDiff = root.getRootView().getHeight() - root.getHeight();
				keyboardListener.isKeyboardShowing(heightDiff>150);
				isProcessing= false;

//				int heightDiff = root.getRootView().getHeight() - root.getHeight();
//				int contentViewTop =activity. getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//
                Log.e(""+root.getRootView().getHeight() +"-"+root.getHeight() +"= "+heightDiff , ""+150);
//
//				keyboardListener.isKeyboardShowing(!(heightDiff <= contentViewTop));
//				isProcessing= false;


			}
		}
	};
	
	
	private void removeGlobalChangeListener(){
		root.getViewTreeObserver().removeOnGlobalLayoutListener(global);
	}
	
	public void stopKeyboardCallbackIfWorking(){
		if(isProcessing){
			removeGlobalChangeListener();
		}
	} 
	
	public void setVisibility(int visibility,View... views){
		removeGlobalChangeListener();
		for(int  i=0;i<views.length ;i++){
			views[i].setVisibility(visibility);
		}
		addGlobalCallbackListener();
	}
	
	public void showKeyboard(EditText edittext){
		input = edittext;
		if(inputMethodManager!=null)
		{
			inputMethodManager.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
		}
		
		addGlobalCallbackListener();
	}

	public void hideKeyboard(){
		if (input==null) return;
		input.setInputType(0);
		    inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
}

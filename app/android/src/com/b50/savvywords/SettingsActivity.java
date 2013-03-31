package com.b50.savvywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

	protected static String[] choices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_settings);

		TextView textView = textViewFor(R.id.settings_description);
		textView.setText(R.string.settings);

		final ListView listView = listViewFor(R.id.words_list);

		if (choices == null) {
			choices = new String[] { "Level 1 (Default)", "Level 2" };
		}
		
		final int[] mappedResource = new int[] { R.raw.words, R.raw.words_2 };

		ListAdapter adpter = new TypefacedAdaptor(this.getApplicationContext(), choices);
		listView.setAdapter(adpter);

		listView.setOnItemClickListener(getClickListener(listView, mappedResource));

	}

	private OnItemClickListener getClickListener(final ListView listView, final int[] mappedResource) {
		return new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String level = ((TextView) view).getText().toString();
				int rawFile = mappedResource[position];

				initializeStudyEngineInstance(rawFile);
				initializeTestingEngineInstance(rawFile);

				showToast("Savvy Words now using " + level);

				if (level.equalsIgnoreCase("Level 2")) {
					choices = new String[] { "Level 1", "Level 2 (selected)" };
				} else { // must be level 1
					choices = new String[] { "Level 1 (selected)", "Level 2" };
				}
				listView.clearChoices();
				listView.setAdapter(new TypefacedAdaptor(getApplicationContext(), choices));
			}
		};
	}

	protected int menuResource() {
		return R.menu.activity_quiz;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class TypefacedAdaptor extends BaseAdapter {

		private final Context context;
		private final List<String> values;

		public TypefacedAdaptor(final Context context, final String[] values) {
			super();
			this.context = context;
			this.values = new ArrayList<String>(Arrays.asList(values));
		}

		@Override
		public int getCount() {
			return this.values.size();
		}

		@Override
		public Object getItem(int arg0) {
			return this.values.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.word_settings_text, viewGroup, false);
			TextView textView = (TextView) rowView.findViewById(R.id.rowTextView);

			Typeface tf = Typeface.createFromAsset(this.context.getAssets(), "ShortStack-Regular.otf");
			textView.setTypeface(tf);
			textView.setText(values.get(position));
			textView.setTextSize(20);
			textView.setTextColor(this.context.getResources().getColor(R.color.black));
			return rowView;
		}

	}

}

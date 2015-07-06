package com.sf.elastic.view;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sf.elastic.R;
import com.sf.elastic.adapters.CityAdapter;
import com.sf.elastic.model.City;
import com.sf.elastic.repository.CityRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_main2)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {

	private CityAdapter cityAdapter;

	@ViewById(R.id.rootLayout)
	public LinearLayout rootLayout;

	@ViewById(R.id.toolbar)
	public Toolbar toolbar;

	@ViewById(R.id.recycler_view)
	RecyclerView recyclerView;

	@ViewById(R.id.cityName)
	public EditText cityName;

	@Bean
	public CityRepository cityRepository;

	@AfterViews
	public void mainAfterViews() {
		initialize();
		searchOnElastic();

		WidgetObservable.text(cityName, false)
			.observeOn(Schedulers.newThread())
			.subscribeOn(AndroidSchedulers.mainThread())
			.debounce(TimeUnit.SECONDS.toSeconds(1), TimeUnit.SECONDS)
			.subscribe(nexTextChangedEventArgs -> {
				runOnUiThread(() -> cityAdapter.clearList());
				cityRepository
					.getNextCity(nexTextChangedEventArgs.text().toString())
					.subscribe(
						city -> {
							runOnUiThread(() -> cityAdapter.add(city));
						});
				});
	}

	@Background
	public void searchOnElastic() {
		cityAdapter.clearList();
		cityRepository
			.getNextCity("")
			.subscribe(
				city -> {
					runOnUiThread(() -> {
						cityAdapter.add(city);
					});
				});
	}

	private void initialize() {
		setSupportActionBar(toolbar);

		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		cityAdapter = new CityAdapter();
		recyclerView.setAdapter(cityAdapter);
	}
}

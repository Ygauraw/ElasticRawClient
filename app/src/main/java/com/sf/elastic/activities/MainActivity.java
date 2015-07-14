package com.sf.elastic.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sf.elastic.R;
import com.sf.elastic.adapters.CityAdapter;
import com.sf.elastic.repositories.CityRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_main2)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {

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

	@Bean
	public CityAdapter cityAdapter;

	@AfterViews
	public void mainAfterViews() {
		initialize();
		searchOnElastic();

//		executePosts();

		WidgetObservable.text(cityName, false)
			.sample(TimeUnit.SECONDS.toSeconds(2), TimeUnit.SECONDS)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(textChangeEvent -> {

				cityAdapter.clearList();

				Log.i(null, "------ BEGIN ------");
				cityRepository
					.getNextCity(textChangeEvent.text().toString())
					.subscribeOn(Schedulers.newThread())
//					.observeOn(AndroidSchedulers.mainThread())
//					.onBackpressureDrop()
					.subscribe(
						city -> {
							runOnUiThread(() -> cityAdapter.add(city));
							Log.i(null, city.getName());
						},
						throwable -> {
							Log.e(null, throwable.getMessage());
						},
						() -> {
							Log.i(null, "------- END -------");
						});
			});
	}

	@Background
	public void executePosts() {
		cityRepository
			.addCities();
	}

	@Background
	public void searchOnElastic() {
		cityAdapter.clearList();
		cityRepository
			.getNextCity("")
//			.subscribeOn(Schedulers.newThread())
			.subscribe(
				city -> {
					runOnUiThread(() -> cityAdapter.add(city));
				});
	}

	private void initialize() {
		setSupportActionBar(toolbar);

		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.setAdapter(cityAdapter);
	}
}

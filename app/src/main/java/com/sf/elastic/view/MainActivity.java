package com.sf.elastic.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sf.elastic.R;
import com.sf.elastic.model.City;
import com.sf.elastic.repository.CityRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.observers.SafeSubscriber;
import rx.schedulers.NewThreadScheduler;
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

	@AfterViews
	public void mainAfterViews() {
		setSupportActionBar(toolbar);

		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		searchOnElastic();

		WidgetObservable.text(cityName, false)
			.observeOn(Schedulers.newThread())
			.subscribeOn(AndroidSchedulers.mainThread())
			.subscribe(nexTextChangedEventArgs -> {
				cityRepository
					.getNextCity(nexTextChangedEventArgs.text().toString())
					.subscribe(
						city -> {
							Log.d("TAG", city.getName());
						});

			});

	}

	@Background
	public void searchOnElastic() {
		cityRepository
			.getNextCity("")
			.subscribe(
				city -> {
					Log.d("TAG", city.getName());
				});
	}
}

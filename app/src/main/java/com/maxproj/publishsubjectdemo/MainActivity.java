package com.maxproj.publishsubjectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

//        observable_just();
//        publish_subject();
//        flowable_emitter();
//        observable_emitter();
//        rxview_click();

    }

    private void rxview_click() {
        RxView.clicks(textView).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                append_str(" " + ((int) (Math.random() * 10)));
            }
        });
    }

    private void observable_emitter() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Integer> e) throws Exception {

                e.setCancellable(new Cancellable() {
                    //when emitter send cancel, textview remove listener?
                    @Override
                    public void cancel() throws Exception {
                        textView.setOnClickListener(null);
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        e.onNext((int) (Math.random() * 10));
                    }
                });
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                append_str(" " + integer);
            }
        });
    }

    private void flowable_emitter() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter<Integer> e) throws Exception {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        e.onNext((int) (Math.random() * 10));
                    }
                });
            }
        }, BackpressureStrategy.MISSING).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {

                append_str(" " + integer);
            }
        });
    }

    private void publish_subject() {
        final PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {

                append_str(" " + integer);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishSubject.onNext((int) (Math.random() * 10));
            }
        });
    }

    private void observable_just() {
        Observable.just("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {

                        append_str(" " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void append_str(String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(textView.getText().toString());
        stringBuilder.append(str2);
        textView.setText(stringBuilder);
    }
}

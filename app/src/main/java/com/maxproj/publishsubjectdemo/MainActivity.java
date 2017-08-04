package com.maxproj.publishsubjectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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


        Observable<Integer> o = Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return null;
            }
        });

//        Observable<Integer> observable = Observable.fromPublisher(new Publisher<Integer>() {
//            @Override
//            public void subscribe(Subscriber<? super Integer> s) {
//
//            }
//        });

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
                publishSubject.onNext((int)(Math.random()*10));
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

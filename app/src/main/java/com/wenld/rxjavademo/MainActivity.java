package com.wenld.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }


            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete!");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe: " + s);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: " );
            }
        };


//        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onNext("Hi");
//                subscriber.onNext("Aloha");
//                subscriber.onCompleted();
//            }
//        });

//        Flowable<String> source = Flowable.fromCallable(() -> {
//            Thread.sleep(1000); //  imitate expensive computation
//            return "Done";
//        });
//
//        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());
//
//        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());
//
//        showForeground.subscribe(System.out::println, Throwable::printStackTrace);


    }
}

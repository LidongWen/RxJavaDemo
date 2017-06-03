package com.wenld.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    Button btn, btn2, btn3;

    String str = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn_activityMain);
        // 需求 需要传一个数据， 先判断本地是否为空 -> 如果为空
        btn.setOnClickListener(v -> {
            Observable.concat(memory, disk, netWork)
                    .subscribeOn(Schedulers.io())//指定Observable 在哪个线程上创建执行操作，如果多次调用，只有第一次有效
                    .observeOn(AndroidSchedulers.mainThread())//在指定下一事件发生的线程
                    .subscribe(observer);
//                    .subscribe(new Consumer<String>() {     //等价于   .subscribe（Observer)
//                        @Override
//                        public void accept(@NonNull String s) throws Exception {
//                            out("accept:" + s);
//                            btn.setText(str);
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(@NonNull Throwable throwable) throws Exception {
//                            out("onError: " + throwable.getMessage());
//                        }
//                    }, new Action() {
//                        @Override
//                        public void run() throws Exception {
//                            out("onComplete: 结束");
//                        }
//                    });
        });

        btn2 = (Button) findViewById(R.id.btn2_activityMain);
        btn2.setOnClickListener(v ->
                Observable.timer(2, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())//指定Observable 在哪个线程上创建执行操作，如果多次调用，只有第一次有效
                        .observeOn(AndroidSchedulers.mainThread())//在指定下一事件发生的线程
                        .subscribe(longObserver)
        );


        btn3 = (Button) findViewById(R.id.btn3_activityMain);
        btn3.setOnClickListener(v ->
                Observable.merge(observer1 -> Observable.just("1", "2", "3"), Observable.just("4", "5", "6"))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer));


        findViewById(R.id.btn4_activityMain).setOnClickListener(v ->
                Observable.interval(2, TimeUnit.SECONDS)
                        .subscribe(longObserver)
        );
    }

    public void out(String msg) {
        System.out.println(msg + "   thead" + Thread.currentThread().getName());
    }

    ObservableSource<String> memory = observer1 -> {
        out("ObservableSource   1:");
        if (str != null) {
            observer1.onNext(str);
        } else {
            observer1.onComplete();
        }
    };
    ObservableSource<String> disk = observer1 -> {
        out("ObservableSource   2:");
        String localData = "localData";
        if (localData != null) {
            observer1.onNext(localData);
        } else {
            observer1.onComplete();
        }
    };
    ObservableSource<String> netWork = observer1 -> {
        out("ObservableSource   3:");
        String netData = null;
        if (netData != null) {
            observer1.onNext(netData);
            observer1.onComplete();
        } else {
            observer1.onComplete();
            observer1.onError(new Throwable("数据为空"));
        }
    };
    Observer observer = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            out("onSubscribe");
        }

        @Override
        public void onNext(String s) {
            out("onNext：" + s);
        }

        @Override
        public void onError(Throwable e) {
            out("onError：" + e.getMessage());
        }

        @Override
        public void onComplete() {
            out("onComplete 结束：");
        }
    };

    Observer<Long> longObserver = new Observer<Long>() {
        @Override
        public void onSubscribe(Disposable d) {
            out("onSubscribe");
        }

        @Override
        public void onNext(Long l) {
            out("onNext：" + l);
        }

        @Override
        public void onError(Throwable e) {
            out("onError：" + e.getMessage());
        }

        @Override
        public void onComplete() {
            out("onComplete 结束：");
        }
    };
}

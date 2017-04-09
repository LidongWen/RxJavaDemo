package com.wenld.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    List<String> list = new ArrayList<>();
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        Observable observable= Observable.just("one", "two", "three", "four", "five");
        observable.subscribeOn(Schedulers.io())//指定Observable 在哪个线程上创建执行操作，如果多次调用，只有第一次有效
                .observeOn(Schedulers.computation())//指定下一个动作发生的线程；
                .doOnNext(new Consumer() {          //
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.e("开始啦", "....."+ o);
                    }
                })
                .map(new Function<String, List<String>>() {     //将数据操作 在发射下去
                    @Override
                    public List<String> apply(String s) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        list.add(s);
                        Log.e("apply", "....."+s);
                        return list;
                    }
                })
//                .flatMap(mOneLetterFunc)
                .observeOn(AndroidSchedulers.mainThread())//在指定下一事件发生的线程
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        tv.setText(""+strings.size());
                    }
                });
    }

    // 设置映射函数
    private Function<String, Observable<String>> mOneLetterFunc = new Function<String, Observable<String>>() {
        @Override
        public Observable<String> apply(@NonNull String strings) throws Exception {
            return  Observable.fromArray(strings,strings);
        }
//        @Override public Observable<String> call(List<String> strings) {
//            return Observable.from(strings); // 映射字符串
//        }
    };
    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
    }
}

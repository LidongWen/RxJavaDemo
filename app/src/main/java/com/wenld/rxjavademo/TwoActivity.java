package com.wenld.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TwoActivity extends AppCompatActivity {
    String TAG = "TwoActivity";
    List<String> list = new ArrayList<>();
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        Observable<String>


//        Observer<String> observer = new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "onSubscribe: " + d);
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d(TAG, "onNext: " + s);
//            }
//
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "Error!");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete!");
//            }
//        };
//
//        Subscriber<String> subscriber = new Subscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                Log.d(TAG, "onSubscribe: " + s);
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d(TAG, "onNext: " + s);
//            }
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "Error!");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete: " );
//            }
//        };


        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subscriber) throws Exception {
                subscriber=new ObservableEmitter<String>() {
                    @Override
                    public void setDisposable(@Nullable Disposable d) {

                    }

                    @Override
                    public void setCancellable(@Nullable Cancellable c) {

                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }

                    @Override
                    public ObservableEmitter<String> serialize() {
                        return null;
                    }

                    @Override
                    public void onNext(@NonNull String value) {
                        Log.d(TAG, "onNext: " + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable error) {
                        Log.d(TAG, "onError: " );
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " );
                    }
                };
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())//指定Observable 在哪个线程上创建执行操作，如果多次调用，只有第一次有效
//                .startWith("新加")
                .observeOn(Schedulers.computation())//指定下一个动作发生的线程；
                .doOnNext(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.e("开始啦", ".....");
                    }
                })
                .map(new Function<String, List<String>>() {     //将数据操作 在发射下去
                    @Override
                    public List<String> apply(String s) {
                        list.add(s);
                        Log.e("apply", "....."+s);
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//在指定下一事件发生的线程
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        tv.setText(""+strings.size());
                    }
                });


//        merge();


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

    private void merge() {
        String TAG="merge";
        Observable<Integer> odds = Observable.just(1, 3, 5).subscribeOn(Schedulers.io());
        Observable<Integer> evens = Observable.just(2, 4, 6);

        Observable.merge(odds, evens)
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                       e=new ObservableEmitter<Integer>() {
                           @Override
                           public void setDisposable(@Nullable Disposable d) {

                           }

                           @Override
                           public void setCancellable(@Nullable Cancellable c) {

                           }

                           @Override
                           public boolean isDisposed() {
                               return false;
                           }

                           @Override
                           public ObservableEmitter<Integer> serialize() {
                               return null;
                           }

                           @Override
                           public void onNext(@NonNull Integer value) {
                               Log.e(TAG,"Next: " + value);
                           }

                           @Override
                           public void onError(@NonNull Throwable error) {
                               Log.e(TAG,"Error: " + error.getMessage());
                           }

                           @Override
                           public void onComplete() {
                               Log.e(TAG,"Sequence complete.");
                           }
                       };
                    }
                });
    }

    private void initView() {
    }
}

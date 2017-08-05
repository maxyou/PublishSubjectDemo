# PublishSubjectDemo
RxJava &amp; RxBinding send user click event

4 examples:

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

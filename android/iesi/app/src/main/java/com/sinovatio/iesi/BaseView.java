package com.sinovatio.iesi;


import com.uber.autodispose.AutoDisposeConverter;

public interface BaseView<T> {

//    void setPresenter(T presenter);

    /**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();

    /**
     * 等待动画开始结束
     */
    void showLoading();
    void hideLoading();
}

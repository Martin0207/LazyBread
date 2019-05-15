package com.martin.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.martin.core.interfaces.ILazyCycle;
import com.martin.core.interfaces.ILazyFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/5 0005
 */
public class LazyBread {

    /**
     * 懒加载集
     */
    private static Map<Fragment, ILazyFragment> sInitMap = new HashMap<>();

    /**
     * 循环集
     */
    private static Map<Object, ILazyCycle> sCycleMap = new HashMap<>();

    /**
     * 绑定Fragment
     */
    public static void bind(final Object obj) {
        if (obj instanceof Fragment) {
            bindLazy((Fragment) obj);
        }
        if (!sCycleMap.containsKey(obj)) {
            String className = obj.getClass().getName();
            ILazyCycle iLazyCycle = null;
            try {
                iLazyCycle = (ILazyCycle) Class.forName(className + "$LazyCycle").newInstance();
                iLazyCycle.init(obj);
                sCycleMap.put(obj, iLazyCycle);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启循环
     */
    public static void resume(Object obj) {
        if (sCycleMap.containsKey(obj)) {
            Objects.requireNonNull(sCycleMap.get(obj)).resume();
        }
    }

    /**
     * 停止循环
     */
    public static void pause(Object obj) {
        if (sCycleMap.containsKey(obj)) {
            Objects.requireNonNull(sCycleMap.get(obj)).pause();
        }
    }

    private static void bindLazy(Fragment fragment) {
        if (!sInitMap.containsKey(fragment)) {
            String className = fragment.getClass().getName();
            try {
                final ILazyFragment iLazyFragment = (ILazyFragment) Class.forName(className + "$LazyInit").newInstance();
                iLazyFragment.init(fragment);
                sInitMap.put(fragment, iLazyFragment);
                (Objects.requireNonNull(fragment.getParentFragment() == null ? fragment.getFragmentManager() : fragment.getParentFragment().getChildFragmentManager()))
                        .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                            @Override
                            public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                                Objects.requireNonNull(sInitMap.get(f)).call();
                            }

                            @Override
                            public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                                Objects.requireNonNull(sInitMap.get(f)).reset();
                            }

                            @Override
                            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            /*
                                这里主动做解除操作
                             */
                                unbind(f);
                            }
                        }, false);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消绑定
     */
    public static void unbind(Object obj) {
        if (obj instanceof Fragment) {
            sInitMap.remove(obj);
        }
        if (sCycleMap.containsKey(obj)) {
            sCycleMap.get(obj).pause();
            sCycleMap.remove(obj);
        }
    }

    /**
     * fragment对用户是否可见
     * 需要用户手动调用
     */
    public static void onUserVisibleHint(Fragment fragment, boolean isUserVisible) {
        if (sInitMap.containsKey(fragment) && isUserVisible) {
            Objects.requireNonNull(sInitMap.get(fragment)).call();
        }
    }

}

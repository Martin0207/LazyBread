## GIF镇楼
![效果图][1]

## 框架引入

在项目根目录的`build.gradle`文件中添加
```gradle
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

app目录下的`build.gradle`文件中添加
```
dependencies {
        implementation 'com.github.Martin0207.LazyBread:core:v1.0.2'
        annotationProcessor 'com.github.Martin0207.LazyBread:processor:v1.0.2'
}
```

## 框架使用
```java
public class TwoFragment extends Fragment {

    private static final String TAG = TwoFragment.class.getSimpleName();
    private TextView mTv;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*
            手动监听Fragment的显隐
         */
        LazyBread.onUserVisibleHint(this, isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
            绑定Fragment
            需要在onActivityCreated或之前调用
         */
        LazyBread.bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.normal_fragment, container, false);
        mTv = inflate.findViewById(R.id.tv);
        return inflate;
    }

    /**
     * 注解方法,实现懒加载
     */
    @LazyInit()
    public void lazy() {
        mTv.setText(TAG);
    }
}
```

## 逻辑图

![逻辑图][2]

## 框架优势

### 1.使用简单

如上图展示，仅需要简单的三步就可以实现Fragment的懒加载功能，并且不破坏原有代码。

### 2.支持懒加载方法排序

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LazyInit {

    /**
     * 是否在每次Fragment对用户可见时都调用
     * 默认情况下，Fragment只需要调用一次初始化方法
     * @return 默认不重复
     */
    boolean isCycle() default false;

    /**
     * 优先值
     * 同一个Fragment中，若拥有多个方法被注解，
     * 则按照优先值由大到小的顺序调用
     * @return 默认为1
     */
    int priority() default 1;

}
```
如源码中展示，可以根据`priority`排列调用顺序。

### 3.支持方法重复调用

代码如上，我们可以设置被LazyInit注解的方法是否在每次触发时调用

### 4.支持Fragment多层嵌套

### 5.支持框架自动解绑

框架绑定有@LazyInit注解方法的Fragment时，会主动监听Fragment的生命周期，并且在`onFragmentDestroyed`时解除Fragment的绑定。当然也你也可以手动解除绑定。

## 结语

框架功能主要由Annotation和AnnotationProcessor来实现，实现方式与思路并不复杂，这里就不做源码分析了。如果有兴趣，可以来看下[**源码**][3]
上文没有太多文字描述内容，我主要还是喜欢在代码备注中表示内容，感觉更直接了当。


  [1]: https://upload-images.jianshu.io/upload_images/4025850-257c4522e64d4a90.gif?imageMogr2/auto-orient/strip
  [2]: https://upload-images.jianshu.io/upload_images/4025850-b7f1a7dbd952721d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240
  [3]: https://github.com/Martin0207/LazyBread

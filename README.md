# Android验证码输入框

## 1、GIF图
![GIF图](https://github.com/pirrip90/InputCodeView/blob/master/screen/screen.gif)

## 2、方法属性
|方法名|参数|描述|
|:---:|:---:|:---:|
| codeTextColor | color |验证码文字颜色(默认为黑色)
| codeTextSize | float |验证码文字大小(默认大小为16sp)
| codeSpace | dimension |验证码输入框的间隙(默认2dp)
| codeBackground | reference |验证码输入框背景图
| codeBackgroundF | reference |将被输入的验证码输入框选中背景图(参考gif图里的最后一个输入例子)
| codeWidth | dimension |验证码输入框宽度
| codeHeight | dimension |验证码输入框高度
| codeNumber | integer |验证码个数(默认为4个)

## 3、方法
|属性名|参数类型|描述|
|:---:|:---:|:---:|
| getInputText | 无 |获取输入的内容
| clear | 无  |清空输入
| setInputText | String |设置输入的内容

## 4、xml使用例子
```xml
    <com.xubo.inputcodeviewlib.InputCodeView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:codeBackground="@drawable/demo3_code_background_n"
        app:codeBackgroundF="@drawable/demo3_code_background_f"
        app:codeNumber="6"
        app:codeSpace="5dp"
        app:codeTextColor="#818181"
        app:codeTextSize="25"
        />
```
## 5、xml使用注意事项
- **textSize**
>textSize属性是float类型，例子中也并没写17sp，所以文字大小是多少sp就写数字多少，支持浮点型
- **codeWidth**
>设置输入框宽度

>一般不用设置，InputCodeView会自动给其分配成文字的高度(使其成为正方形)
- **codeHeight**
>设置输入框高度

>和codeWidth属性一样，一般不用设置，InputCodeView会自动给其分配成文字的高度

>如果设置输入框的高度不足以达到输入框文字高度,则设置的高度将无效，输入框高度将保持文字的高度
- **codeBackground**
>设置输入框的背景图

>如果该属性未设置，也意味着codeBackgroundF设置的属性将无效（codeBackground和codeBackgroundF将使用默认的背景图）
- **codeBackgroundF**
>设置被将被输入的输入框的选中背景图

>如果该属性未设置，则将被输入的输入框不会带有选中效果，输入框全部使用codeBackground
- **layout_width和layout_height**
>设置wrap_content即可




## 6、gradle
add the dependency:
```gradle
dependencies {
    ...
    
    implementation 'com.xubo.inputcodeviewlib:InputCodeView:1.0.1'
}
```
**Android Studio 3.0以上可以使用implementation代替compile,低于3.0依然使用compile**


## 7、代码编译失败？
1.删除项目根目录gradle.properties里的`systemProp.http.proxyHost=127.0.0.1`和`systemProp.http.proxyPort=1080`
>gradle.properties里的代理信息不是你本机代理配置,请删除或改成你本机代理地址和端口

2.Android Studio更新到3.0






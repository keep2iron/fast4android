混淆规则

# activity
-keep class io.github.keep2iron.android.core.AbstractActivity { *; }

# 不混淆使用了StatueColor的类
-keep class io.github.keep2iron.android.annotation.StatusColor { *; }
-keep @io.github.keep2iron.android.annotation.StatusColor class * {*;}
-keep class * implements java.lang.annotation.Annotation {*;}

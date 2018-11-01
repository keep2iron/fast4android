package io.github.keep2iron.app

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.android.utilities.WeakHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/1
 */
class WebviewScrollActivity : AbstractActivity<ViewDataBinding>() {


    val webView: WebView by FindViewById(R.id.webView)

    override fun getResId(): Int = R.layout.activity_webview_scroll

    val weakHandler = WeakHandler()

    override fun initVariables(savedInstanceState: Bundle?) {
        val bufferedReader = BufferedReader(InputStreamReader(assets.open("index.html"),"UTF-8"))
        var line: String?
        val sb = StringBuilder()
        do {
            line = bufferedReader.readLine()
            if (!line.isNullOrEmpty()) {
                sb.append(line)
            }
        } while (!line.isNullOrEmpty())
        bufferedReader.close()

        var content = "<p>今天俺心情好，来给大家讲个loser小鸟的逆袭故事。</p>\n" +
                "<p>他是个壮（pang）汉（zi）。每次远远看着他走来都会有种一坨肉在向我移动，另外偷偷告诉你，我刚认识他的时候，第一眼打量他以后，脑子里就只有两个字“小鸟”。</p><p>“小鸟”和我交情还不错，人挺健谈，就是老爱吹牛逼，经常在人堆里吹嘘他的前戏花样多，啪啪技巧好，能让妞合不了腿，下不了床，不过每次我们几个人在谈论自己尺寸大小的时候，他就不怎么吱声了。</p><p>某一天，正当我们在叙旧的时候，他忽然对我说“性生活不和谐肿么办？”我着实被呛了一下，What!? 不和谐？我没听错吧！床技大湿也会有不和谐的时候？！</p><p>“怎么个不和谐？说说看。” </p><p>“最近不行了，和我老婆经常因为这个事情吵架。”</p><p>“怎么不行了？”  </p><p>“不就是因为那个。”</p><p> “哪个？”</p><p> “那个！”</p><p> “哪个！”</p><p> “TMD，你特么磨磨唧唧，倒是是啥快说！”</p><p> “哎，就是鸡鸡，她嫌我小……”</p><p>(⊙o⊙)额，这真是个残酷的现实。。。。。。</p><p>后来聊了下，果真我的“第一直觉”没错，确实是“小鸟”。</p><p> </br>\n" +
                "<img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/318/481/3689d67795.jpg></center>\n" +
                "</br></p><p>“小鸟”说：“几乎每次做，套套都很容易脱落，开始还以为是套的问题，后面才发现，原来是额，丁丁太小。。。。最近好几次都脱落在她里面，要拿出来都会扣到她疼，被她白眼了好几回，又担心怀孕也嫌弃我小，最近做完都老是为套套脱落这事吵，现在她都不愿意做了……”</p><p>这，苍天啊！咳咳，这东北大汉，多粗壮的样子。180cm的身高，180平的房子 ，却没有180的……</p><p>这种情况，你不知道应该用安全套有出小号的吗？“小号套？”额……好吧。我们不应该这么叫。</p><p>紧绷型，紧型装，这样会显得专业点精致点有木有！</p><p>在这里，我必须得严重！强调！一下我用的是普！通！套！是因为经历过朋友的这些倾诉，感觉这种情况也会是很多朋友的困惑。于是去周边留意了一下，发现比较常见且好买到的紧绷型的套套有【杜蕾斯】和【倍力乐】这两个牌子，应该还有其他，但我没细找。</p><p> </br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/739/964/2d478af1fb.jpg></br></p><p>我拿了一个自己偶尔会用用的普通size的杰士邦zero套套来做参考对比。</p><p>仔细看的话，肉眼还是能看得出【杜蕾斯的紧型装】和【倍力乐的紧绷套】会比杰士邦的要小一圈。</p><p> </br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/a5d/b44/addad479bb.jpg></br></p><p> </br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/e4c/260/fdb707b8f5.jpg></br></p><p>一般普通标准size的套套，周长都是52mm左右。</p><p>这次我没有量周长，而是只用量尺量了一下直径宽度。</p><p>先量了这个杜蕾斯粉色的，直径宽度大概35mm。</p><p> </br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/219/c88/d500325479.jpg></br></p><p>倍力乐的紧绷套在包装里稍微有些压扁了。量了一下也接近35mm。</p><p> </br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/d77/d31/f1baf4a7c7.jpg></br></p><p>来量一下普通标准的套套，直径宽度是38mm。</p><p> </br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/436/710/c613d855b3.jpg></br></p><p>所以这么算的话，如果“小鸟”想要戴合适的套来冒充「普通标准」size的“标准适用鸟”的话，应该要戴6层……（额，突然觉得有种淡淡的忧伤）</p><p>来对比一下这两款紧绷型的套套：</p><p></br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/c20/f61/73cdecce21.jpg></br> </p><p></br><img src=http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/up/563/024/81f3810b5b.jpg></br></p><p>1.\t气味：</p><p>【杜蕾斯】的味道比较清淡，【倍力乐】有股较浓郁的橡胶花香味？（什么鬼）</p><p>不喜欢香味重的建议选择杜蕾斯的。</p><p>2.\t颜色：</p><p>【杜蕾斯】粉色的显得很有存在感，但是【倍力乐】的暗黄色也没能让我有多少好感，不过其实这个不太影响。都已经进入蜜园了，谁还管颜色是什么呢，嘿嘿嘿……</p><p>3.\t外观：</p><p>【杜蕾斯】的紧型装是平滑的，【倍力乐】的紧绷套是有带颗粒的。个人还是会建议带颗粒的，感觉会给“小鸟”加分，嗯！</p><p>4.\t长度：</p><p>【杜蕾斯】的会比【倍力乐】的长一些，但这有必要吗？“长”却“细”的话……额，在我脑海里怎么就浮现出铅笔的模样……</p><p>5.\t润滑油：</p><p>【杜蕾斯】紧型装上面附着的油较为均匀，不算多。【倍力乐】紧绷套上面附着的油较多，但分布的不太均匀，大致中段以后就没有多少润滑了。</p><p>果不其然，估计他听完之后兴冲冲地去买了然后实战了好几回，现在看起来那叫一个满面春风啊。</p><p>其实丁丁小点不是什么致命伤，前戏足，花样多，床技好也还是可以弥补的，抽插运动不是女人唯一看重的，更多的是全方位的刺激满足。</p><p>就像我这位“上身壮如牛、下面小如鸟”的朋友，个人估约这种情况也不是罕例。</p><p>但是选择合适的套套确实不是开玩笑。</p><p>避孕套脱落，“尺寸”不配套是主因</p><p>　　要搞清楚造成套套脱落在阴道内的原因有三种：</p><p>　　A、使用的避孕套过大，不合适</p><p>　　B、男方在完事后没有及时将丁丁和套套套一起抽出，软了以后造成的套套脱落</p><p>C、精液过多，精液本身有润滑的作用，完事后抽出时一定要捏紧套口，否则也是很容易引起脱落。</p><p>很多情侣都经历过套套滑落的尴尬。发生这样的事完全不必紧张，正确的做法是立即停止运动，采取一些积极的补救措施。</p><p>首先，让女性伴侣蹲下，使精液从其体内流出。</p><p>然后，再采取以下措施：女性冲洗阴户、服用紧急避孕药。</p><p>　　假如套套脱落在蜜道内，但套口还在外面，只需轻轻捏住其根部拽出即可。</p><p>记住，不要灌洗蜜道，那样反而会把小蝌蚪推向深处。</p><p>额，如果完全掉落在里面了，拽不出。运气好的话，用手指应该还可以抠出来，运气不好的话……那就做好给医生教育的准备吧。</p></center>"

        val bodyTag = "<body>"
        sb.insert(sb.indexOf(bodyTag) + bodyTag.length, content)

//        webView.loadUrl("http://10.3.0.246:8769/R-FEEDBACK/feedback/add?clienid=12312312312&os=ios&userid=13853531351&colorCode=ffd21e")
        Logger.d(sb.toString())
        webView.loadData(sb.toString(), "text/html", "UTF-8")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                Logger.d("onPageFinished ")
                view.evaluateJavascript("(function() { return window.outerHeight; })();") { value ->
                    Logger.d("value $value")
                    weakHandler.post(ChangeWebViewRunnable(webView, value.toInt()))
                }
            }
        }
        val webSettings = webView.settings
        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.setAppCacheEnabled(false)

        // 设置可以支持缩放
//            webSettings.setSupportZoom(true);
        // 设置出现缩放工具
//        webSettings.builtInZoomControls = false
//        //不显示webview缩放按钮
//        webSettings.displayZoomControls = false
////            扩大比例的缩放
////            webSettings.setUseWideViewPort(true);
//        //自适应屏幕
//        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        webSettings.loadWithOverviewMode = true
    }

    internal inner class ChangeWebViewRunnable(private val webView: WebView, private val height: Int) : Runnable {
        private var parent: ViewGroup? = null
        private val displayMetrics: DisplayMetrics

        init {
            parent = webView.parent as ViewGroup
            displayMetrics = webView.context.resources.displayMetrics
        }

        override fun run() {
            val value = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, height.toFloat(),
                    displayMetrics)

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, value.toInt())

            parent?.apply {
                val index = this.indexOfChild(webView)
                removeView(webView)
                addView(webView, index, params)
                layoutParams.height = value.toInt()
            }
        }
    }
}
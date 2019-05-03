package io.github.keep2iron.app.valyout

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.webkit.*
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.utilities.WeakHandler
import io.github.keep2iron.app.R
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/1
 */
class WebViewAdapter(val ctx: Context, val adapter: DelegateAdapter) : AbstractSubAdapter( 2) {
    val weakHandler = WeakHandler()

    private var webView: WebView? = null

    /**
     * 用于判断是否加载过
     */
    private var isLoaded = false

    private var isMeasureHeight = false

    private var videoTagCount = 0

    private val indexHtml = StringBuilder()

    override fun getLayoutId(): Int = R.layout.item_web_view

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val viewHolder = super.onCreateViewHolder(parent, viewType)
        val webView = viewHolder.itemView as WebView

        val bufferedReader = BufferedReader(InputStreamReader(ctx.assets.open("index.html"), "UTF-8"))
        var line: String?
        do {
            line = bufferedReader.readLine()
            if (!line.isNullOrEmpty()) {
                indexHtml.append(line)
            }
        } while (line != null)
        bufferedReader.close()

        setUpWebViewDefaults(webView)

        return viewHolder
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int, payloads: MutableList<Any>) {
        val content = "<video width=\"100%\" height=\"300px\" style=\"background:#000;min-height:35vh;max-height:35vh;\" src=\"https://video.bsays.net/d5beed297ac84cf0b199b7803d8b5fa8/5cb5b6475a36433680a3087b2db339cc-35a011d4060be1931eb48da394ef968b-ld.mp4\" controls=\"controls\"></video><p><br></p><p><br></p><p>这个帖子介绍无字表白情书大全，本帖不断收集这种无字情书的制作方法，如果你有这方面的浪漫点子，请向我们投稿。</p ><p>一封情书经过特殊工艺的制作，送到她手里的时候是白纸一张，告诉她一个简单的方法可以看到里面的字，她肯定好奇的去做，于是看到里面暗藏的秘密。该点子单独用可能比较单调，可以融入浪漫计划中。</p ><p>准备一个橘子，一张白纸，一张卡片，一支牙签（棉签），一个打火机。" +
                "1、将橘子挤出汁。" +
                "2、用牙签或者棉签，沾了橘汁，在 白纸上轻轻写下你对女朋友想说的话，待干以后，不留痕迹，还是白纸一张。" +
                "3、为了增加神秘感，可以附上一张卡片作为提示，上面可以写着，如：白纸一张，暗藏玄机。待到情浓火热时，庐山可显，真心可表。" +
                "4、将白纸和卡片装到信封中，顺便放入一个打火机。" +
                "5、如果对方理解了卡片上的提示，用打火机轻轻烤一下白纸，你想对女朋友说的话，浪漫的表白都自会从白纸中显现出来。</p ><p>这个点子，既花了心思，又充满神秘感，还有一首浪漫的诗，耐人寻味！</p><p>想一想，如果送给她一张白纸，她肯定会摸不着头脑。然后你将内容变出来，她一定会觉得很神奇的。\n" +
                "这种表白一定很别出心裁，难以忘记。若再配合特定场景，特别发挥，效果会更好。</p><p>那就按照下面的步骤试一试吧，每一个有心人，都会成为浪漫的人的。</p ><p><img src=\"http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/romantic/1540819289689wuziqingshu-1.jpg\"></p><p><img src=\"http://bana-spider-image.oss-cn-shenzhen.aliyuncs.com/romantic/1540819289759wuziqingshu-2.jpg\">"

        if (isMeasureHeight) {
            val height = payloads[0] as Int
            val itemView = holder.itemView as ViewGroup

            val params = RecyclerView.LayoutParams(itemView.layoutParams.width, height)
            itemView.layoutParams = params

            val webView = holder.findViewById<WebView>(R.id.webView)
            webView.loadData(indexHtml.toString(), "text/html", "UTF-8")
        }

        if (!isLoaded && !content.isNullOrEmpty()) {
            val webView = holder.findViewById<WebView>(R.id.webView)
            this.webView = webView

            val bodyTag = "<body>"
            indexHtml.insert(indexHtml.indexOf(bodyTag) + bodyTag.length, content)

            webView.loadData(indexHtml.toString(), "text/html", "UTF-8")
            isLoaded = true
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun setUpWebViewDefaults(webView: WebView) {
        webView.webChromeClient = WebChromeClient()

        val settings = webView.settings

        // Enable Javascript
        settings.javaScriptEnabled = true

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        // Enable pinch to zoom without the zoom buttons
        settings.builtInZoomControls = true

        // Allow use of Local Storage
        settings.domStorageEnabled = true

        // Hide the zoom controls for HONEYCOMB+
        settings.displayZoomControls = false

        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        settings.mediaPlaybackRequiresUserGesture = false
        webView.addJavascriptInterface(this, "MyApp")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                (ctx as Activity).runOnUiThread {
                    isMeasureHeight = true
                    notifyItemChanged(0, view.measuredHeight)
                }
                webView.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)")
                super.onPageFinished(view, url)
            }
        }
    }

//    @JavascriptInterface
//    fun resize(height: Float) {
//        (ctx as Activity).runOnUiThread {
//            Logger.d("resize height : $height")
//        }
//    }

    override fun getItemCount(): Int = 1

    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()
}
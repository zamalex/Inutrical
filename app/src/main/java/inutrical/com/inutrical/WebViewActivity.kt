package inutrical.com.inutrical

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.webviewtopdf.PdfView
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class WebViewActivity : AppCompatActivity() {

    lateinit var loading : android.app.AlertDialog

    lateinit var webView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        loading = SpotsDialog.Builder().setContext(this).build()

        webView = findViewById(R.id.webview)

        back_btn.setOnClickListener {
            onBackPressed()
        }


        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.

        // this will load the url of the website

        webView.settings.javaScriptEnabled=true
        webView.settings.domStorageEnabled=true

        webView.loadData(intent.getStringExtra("html")!!,  "text/html; charset=UTF-8;", null)

        // this will enable the javascript settings
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)

        val directory = Environment.getExternalStoragePublicDirectory(
            Environment.
            DIRECTORY_DOWNLOADS+""
        )
        val fileName = "diet_plan.pdf"



        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loading.show()
                PdfView.createWebPrintJob(
                    this@WebViewActivity,
                    webView,
                    directory,
                    fileName,
                    object : PdfView.Callback {
                        override fun success(path: String) {
                            loading.dismiss()
                            try {
                                val file = File(path)
                                val share = Intent()
                                share.action = Intent.ACTION_SEND
                                share.type = "application/pdf"
                                share.putExtra(
                                    Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                                        this@WebViewActivity,
                                        BuildConfig.APPLICATION_ID.toString() + ".provider",
                                        file
                                    )
                                )
                                startActivity(Intent.createChooser(share, "Share"))


                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(this@WebViewActivity, "No PDF Viewer Installed", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }

                        override fun failure() {
                            loading.dismiss()
                        }
                    })
            }
        })







    }

    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}


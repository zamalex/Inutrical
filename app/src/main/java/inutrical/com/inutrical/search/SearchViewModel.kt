package inutrical.com.inutrical.search

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import inutrical.com.inutrical.RequestDietPlan
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class SearchViewModel : ViewModel() {

    var result = MutableLiveData<SearchModel>()
    var history = MutableLiveData<HistoryModel>()
    var pdfresponse = MutableLiveData<Boolean>()
    var htmlReponse = MutableLiveData<ResponseBody>()


    fun search(jsonObject: JsonObject) {
        inutrical.com.inutrical.api.Retrofit.Api.search(jsonObject)
            .enqueue(object : Callback<SearchModel> {
                override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                    Log.e("error",t.localizedMessage);
                    result.value = SearchModel().apply { statusCode = 555 }
                }

                override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {
                    result.value = response.body()

                }
            })
    }


    fun getHistory(jsonObject: JsonObject) {
        inutrical.com.inutrical.api.Retrofit.Api.getHistory(jsonObject)
            .enqueue(object : Callback<HistoryModel> {
                override fun onFailure(call: Call<HistoryModel>, t: Throwable) {
                    history.value = HistoryModel().apply { errorCode = 555 }
                }

                override fun onResponse(
                    call: Call<HistoryModel>,
                    response: Response<HistoryModel>
                ) {
                    history.value = response.body()
                }
            })
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            val exportedFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/diet_plan" + ".pdf"
            )
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                //val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(exportedFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }

    fun exportPdf(requestObj: RequestDietPlan) {
        inutrical.com.inutrical.api.Retrofit.Api.exportPdf(requestObj)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    htmlReponse.value = null
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                       // pdfresponse.value = writeResponseBodyToDisk(response.body()!!)
                        htmlReponse.value = response.body()
                    else
                        htmlReponse.value = null
                }
            })
    }


    fun downloadFile(url: String, context: Context,name:String) {

        val request1 =
            DownloadManager.Request(Uri.parse(url))
        request1.setDescription("Downloading file") //appears the same in Notification bar while downloading
        request1.setTitle("${name}.pdf")
        request1.setVisibleInDownloadsUi(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner()
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        request1.setDestinationInExternalFilesDir(context, "/File", "${name}.pdf")
        val manager1 =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager1.enqueue(request1)
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Log.v("pdf3:", "all done")
            Toast.makeText(context,"Downloading",Toast.LENGTH_SHORT).show()
        } else             Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()

    }
}
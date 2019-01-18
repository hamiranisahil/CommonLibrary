package com.example.common.api_call

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import android.support.annotation.StringDef
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.library.BuildConfig
import com.example.library.modals.CommonRes
import com.example.library.topsnackbar.MySnackbar
import com.example.library.util.AppConfig
import com.example.library.util.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.*
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import java.util.concurrent.TimeUnit


class ApiCallKotlin(
    val context: Context,
    val requestParams: Array<Any>,
    val paramsBody: Any, @WebServiceType.Type val webServiceType: String,
    val retrofitResponseListener: ApiCallKotlin.RetrofitResponseListener
) {

    internal var url = ""
    var method = ""
    var requestCode = -1
    internal var isSuccess = false
    private var fileDownloadPath: String? = null
    private var rootView: View? = null

    private val client: Retrofit
        get() {
            if (retrofit == null) {
                val okhttpBuilder = OkHttpClient.Builder()
                okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS)
                okhttpBuilder.readTimeout(60, TimeUnit.SECONDS)
                okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS)
                retrofit = Retrofit.Builder()
                    .baseUrl(Companion.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpBuilder.build())
                    .build()
            }
            return retrofit!!
        }

    init {
        url = requestParams[0] as String
        method = requestParams[1] as String
        requestCode = requestParams[2] as Int

        rootView = (context as Activity).window.decorView.rootView.findViewById(android.R.id.content)
        call()
    }

    fun setFileDownloadPath(fileDownloadPath: String) {
        this.fileDownloadPath = fileDownloadPath
    }


    private fun call() {
        if (isOnline()) {
            val progressView = LayoutInflater.from(context).inflate(R.layout.loading_progress, null)
            if (!LOADING_TITLE.isEmpty()) {

                //                progressView.tvLoading.text = LOADING_TITLE
            }

            val rootView: View = (context as Activity).window.decorView.rootView.findViewById(android.R.id.content)

            val frameLayout: FrameLayout = rootView as FrameLayout
            frameLayout.addView(progressView)

            val apiInterface: ApiInterface? = client.create(ApiInterface::class.java)

            val jsonString = Gson().toJson(paramsBody)

            if (HEADER_MAP == null) {
                HEADER_MAP = HashMap()
            }

            var responseCall: Call<ResponseBody>? = null

            if (method.equals(ApiCallKotlin.RequestType.GET, true)) {
                if (paramsBody is String) {
                    responseCall = apiInterface?.get(HEADER_MAP!!, url + paramsBody, getMapFromGson(null))
                } else {
                    responseCall = apiInterface?.get(HEADER_MAP!!, url, getMapFromGson(jsonString))
                }
            } else if (method.equals(RequestType.POST, true)) {
                responseCall = apiInterface?.postRaw(
                    HEADER_MAP!!,
                    url,
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString)
                )
            }

            if (responseCall != null) {
                responseCall.enqueue(object : Callback<ResponseBody> {

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        frameLayout.removeView(progressView)
                        try {
                            var bodyString = ""
                            var responseBody: ResponseBody? = null

                            if (webServiceType.equals(WebServiceType.WS_FILE_DOWNLOAD) || webServiceType.equals(
                                    WebServiceType.WS_FILE_DOWNLOAD_WITH_MESSAGE
                                )
                            ) {
                                responseBody = response.body()
                                if (responseBody == null) {
                                    retrofitResponseListener.onSuccess(null, requestCode)
                                    return
                                }

                            } else {
                                bodyString = response.body()?.string()!!
                            }

                            if (BuildConfig.DEBUG) {
                                Log.e(
                                    "ApiCall - Request",
                                    "Url: $url Method: $method RequestCode: $requestCode WebServiceType: $webServiceType FileDownloadPath: $fileDownloadPath"
                                )
                                Log.e("ApiCall - Request", "ParamsBody: $jsonString")
                                Log.e("ApiCall - Response", "ParamsBody: $bodyString")
                            }
                            var commonRes = Gson().fromJson(bodyString, CommonRes::class.java)
                            when (response.code()) {
                                AppConfig.STATUS_200 -> {
                                    if (webServiceType.equals(WebServiceType.WS_FILE_DOWNLOAD) || webServiceType.equals(
                                            WebServiceType.WS_FILE_DOWNLOAD_WITH_MESSAGE
                                        )
                                    ) {
                                        saveResponseToDisk(responseBody!!, paramsBody as String)

                                        if (webServiceType.equals(WebServiceType.WS_FILE_DOWNLOAD_WITH_MESSAGE)) {
                                            MySnackbar.create(
                                                context,
                                                "File Download Successfully",
                                                MySnackbar.GRAVITY_TOP,
                                                MySnackbar.DURATION_LENGTH_LONG
                                            ).show()
                                        }

                                        retrofitResponseListener.onSuccess(
                                            fileDownloadPath + "/" + paramsBody,
                                            requestCode
                                        )

                                    } else {
                                        retrofitResponseListener.onSuccess(bodyString, requestCode)
                                    }
                                }
                                AppConfig.STATUS_204 -> {
                                    MySnackbar.create(
                                        context,
                                        commonRes.message,
                                        MySnackbar.GRAVITY_BOTTOM,
                                        MySnackbar.DURATION_LENGTH_LONG
                                    ).show()
                                }
                                AppConfig.STATUS_409 -> {

                                }
                                AppConfig.STATUS_404 -> {
//                                    setNoDataFound();
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        frameLayout.removeView(progressView)

                        if (t is SocketTimeoutException) {
                            handleNoInternetTimoutDialog(context.getString(R.string.timeout))
                        }
                        if (t is UnknownServiceException) {
                            if (t.message!!.contains("CLEARTEXT")) {
                                MySnackbar.create(
                                    context,
                                    "API Level 28 > CLEARTEXT Support Disabled..",
                                    MySnackbar.GRAVITY_TOP,
                                    MySnackbar.DURATION_LENGTH_INDEFINITE
                                ).show()
                            }
                        }

                        retrofitResponseListener.onFailure(t, requestCode)
                    }
                })

            }
        } else {
            handleNoInternetTimoutDialog((context.getString(R.string.no_internet)))
        }
    }

    private fun removeNoDataFound() {
//        (rootView!!.findViewWithTag("root_layout") as ViewGroup)
    }

    private fun setNoDataFound() {
        LayoutInflater.from(context)
            .inflate(R.layout.no_data_found, (rootView as ViewGroup).findViewWithTag("root_layout"), true)
    }

    private fun saveResponseToDisk(responseBody: ResponseBody, url: String): Boolean {

        Util.askPermissions(context, 1, Util.AppPermissionListener {
            //                Toast.makeText(context, "Permission Granted..", Toast.LENGTH_SHORT).show();
            try {
                if (fileDownloadPath == null || fileDownloadPath!!.isEmpty()) {
//                    fileDownloadPath = context.getCacheDir().path
                    fileDownloadPath = Environment.getExternalStorageDirectory().path
                }
                val downloadFile = File(fileDownloadPath, url.substring(url.lastIndexOf('/') + 1))

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try {
                    val fileReader = ByteArray(4096)
                    val fileSize = responseBody.contentLength()
                    var fileSizeDownloaded: Long = 0

                    inputStream = responseBody.byteStream()
                    outputStream = FileOutputStream(downloadFile)

                    while (true) {
                        val read = inputStream!!.read(fileReader)
                        if (read == -1) {
                            break
                        }

                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()

                    }
                    outputStream.flush()
                    isSuccess = true

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }, false, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

        return isSuccess
    }

    fun handleNoInternetTimoutDialog(type: String) {
        var view: View? = null
        var dialog: Dialog? = null

        if (type.equals(context.getString(R.string.timeout), true)) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_timeout, null)
        } else if (type.equals(context.getString(R.string.no_internet), true)) {
            view = LayoutInflater.from(context).inflate(R.layout.no_internet, null)
        }

        if (DIALOG_FULLSCREEN) {
            dialog = Dialog(context, R.style.full_screen_dialog)
            dialog.setContentView(view!!)
            dialog.setCancelable(false)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.show()
        }

        val ivRetry: ImageView = view!!.findViewById(R.id.ivRetry)
        ivRetry.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (isOnline()) {
                    if (dialog != null && dialog.isShowing) {
                        dialog.dismiss()
                    }
                    call()
                } else {
                    Toast.makeText(context, "Check Your Connection..!", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun getMapFromGson(json: String?): Map<String, Any> {
        if (json != null && !json.equals("null", true) && !json.equals("{}", true)) {
            return Gson().fromJson(json, object : TypeToken<HashMap<String, Any>>() {}.type)
        }
        return HashMap<String, String>()
    }

    fun isOnline(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return if (networkInfo != null) networkInfo.isConnected else false
    }

    interface RetrofitResponseListener {
        fun onSuccess(response: String?, apiRequestCode: Int)
        fun onFailure(t: Throwable, apiRequestCode: Int)
    }

    interface RetrofitDownloadResponseListener {
        fun onDownload(path: String, apiRequestCode: Int)
    }

    @JvmSuppressWildcards
    internal interface ApiInterface {

        @POST
        fun postRaw(@HeaderMap mapHeader: Map<String, Any>, @Url url: String, @Body requestBody: RequestBody): Call<ResponseBody>

        @GET
        fun get(@HeaderMap mapHeader: Map<String, Any>, @Url url: String, @QueryMap queryMap: Map<String, Any>): Call<ResponseBody>

    }

    class RequestType {

        @StringDef(GET, POST)
        annotation class Type

        companion object {
            const val GET = "get"
            const val POST = "post"
        }
    }

    class WebServiceType {

        @StringDef(WS_SIMPLE, WS_SIMPLE_WITH_MESSAGE, WS_FILE_DOWNLOAD, WS_FILE_DOWNLOAD_WITH_MESSAGE)
        annotation class Type

        companion object {
            const val WS_SIMPLE = "ws_simple"
            const val WS_SIMPLE_WITH_MESSAGE = "ws_simple_with_message"
            const val WS_FILE_DOWNLOAD = "ws_file_download"
            const val WS_FILE_DOWNLOAD_WITH_MESSAGE = "ws_file_download_with_message"
        }
    }

    companion object {
        var BASE_URL = ""
        var HEADER_MAP: Map<String, Any>? = null
        var LOADING_TITLE = ""
        var DIALOG_FULLSCREEN = true
        var retrofit: Retrofit? = null
    }

}
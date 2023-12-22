package com.manga.mangahubapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.response.ChapterResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class ChapterInfo : AppCompatActivity() {

    private val activity: AppCompatActivity = this@ChapterInfo
    private val apiRepository = ApiRepositoryImpl()
    private var token: String? = null
    private var chapterId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_info)
        getExtra()
        token = MainPage.getToken()
        getChapter();
    }

    private fun getExtra() {
        val arguments = intent.extras
        if (arguments != null) {
            if (arguments.containsKey("chapterId")) {
                chapterId = arguments.getString("chapterId")
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }
    }

    private fun getChapter() {
        apiRepository.getChapter("Bearer " + token, chapterId!!, object :
            Callback<ChapterResponse> {
            override fun onResponse(
                call: Call<ChapterResponse>,
                response: Response<ChapterResponse>
            ) {
                if (response.isSuccessful) {
                    val dataList = response.body()
                    if (dataList != null) {
                        var title = findViewById<TextView>(R.id.titleChapter)
                        title.setText(dataList.title)
                        renderPdf(dataList.scans.toString())
//                        saveBase64PdfToStorage(dataList.scans.toString(), "scanned.pdf")
//                        displayPdfFromStorage(activity, "scanned.pdf")
                    } else {
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ChapterResponse>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun saveBase64PdfToStorage(base64String: String, fileName: String) {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        val filePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, fileName)

        try {
            val outputStream = FileOutputStream(file)
            outputStream.write(decodedBytes)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun displayPdfFromStorage(context: Context, fileName: String) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )

        val pdfUri = FileProvider.getUriForFile(this, "com.manga.mangahubapp.fileprovider", file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(pdfUri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        val chooser = Intent.createChooser(intent, "Open with")
        context.startActivity(chooser)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
        }
    }

    private fun renderPdf(base64EncodedString: String) {

        val decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT)
        var pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView.fromBytes(decodedBytes).load()

    }

}
package com.example.facedetection

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.facedetection.databinding.ActivityMainBinding
import com.example.facedetection.databinding.DocumentActivityBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.ScannerMode
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class DocumentReader: AppCompatActivity(R.layout.document_activity) {

    private lateinit var binding: DocumentActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DocumentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

            scannerBuilder()

    }

    private val GmsOption = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(true)
        .setPageLimit(2)
        .setResultFormats(RESULT_FORMAT_JPEG,RESULT_FORMAT_PDF)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()

    private fun scannerBuilder() {
        val scanner = GmsDocumentScanning.getClient(GmsOption)

        val scannerLauncher = this.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                scanningResult?.pages?.let { pages ->
                    for (page in pages) {
                        val imageUri = pages[0].imageUri

                        // Process the image to extract text
                        val image = InputImage.fromFilePath(this, imageUri)
                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                // Process the extracted text
                                val extractedText = visionText.text
                                val jsonObject = JsonObject().apply {
                                    addProperty("text", extractedText)
                                }

                                // Convert the JSON object to a JSON string
                                val jsonExtractedText = jsonObject.toString()
                                Log.d("ExtractedText", Gson().toJson(extractedText).toString())
                                binding.tvResult.text = /*Gson().toJsonTree(extractedText).toString()*/
                                    jsonExtractedText


                                // You can now use the extracted text as needed
                            }
                            .addOnFailureListener { e ->
                                // Handle the error
                                Log.e("TextRecognition", "Text recognition failed", e)
                            }
                    }
                }

                scanningResult?.getPdf()?.let { pdf ->
                    val pdfUri = pdf.uri
                    val pageCount = pdf.pageCount
                    // If you need to extract text from PDF, you'll need a separate PDF text extraction library
                }
            }
        }

// Get the Intent to start document scanning
        scanner.getStartScanIntent(this@DocumentReader)
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener {
                // Handle the failure
            }
       /* val scanner = GmsDocumentScanning.getClient(GmsOption)
        val scannerLauncher = this@DocumentReader.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                result ->
            run {
                if (result.resultCode == RESULT_OK) {
                    val result =
                        GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                    result?.getPages()?.let { pages ->
                        for (page in pages) {
                            val imageUri = pages.get(0).getImageUri()
                        }
                    }
                    result?.pdf?.let { pdf ->
                        val pdfUri = pdf.uri
                        val pageCount = pdf.pageCount
                    }
                }
            }
        }

        scanner.getStartScanIntent(this@DocumentReader)
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener {
                Toast.makeText(this, "OCR failed ",Toast.LENGTH_SHORT).show()
            }
*/
    }
}
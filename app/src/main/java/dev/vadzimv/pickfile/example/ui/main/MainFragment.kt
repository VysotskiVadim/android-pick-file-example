package dev.vadzimv.pickfile.example.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import dev.vadzimv.pickfile.example.OpenFileResult
import dev.vadzimv.pickfile.example.R
import dev.vadzimv.pickfile.example.openDocumentPicker
import dev.vadzimv.pickfile.example.tryHandleOpenDocumentResult
import java.io.InputStream

class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()

        private const val TAG = "MainFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pickFileButton = view.findViewById<Button>(R.id.pickFileButton)
        pickFileButton.setOnClickListener {
            openDocumentPicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (val result = tryHandleOpenDocumentResult(requestCode, resultCode, data)) {
            OpenFileResult.DifferentResult, OpenFileResult.OpenFileWasCancelled -> { }
            OpenFileResult.ErrorOpeningFile -> Log.e(TAG, "error opening file")
            is OpenFileResult.FileWasOpened -> fileIsOpened(result.fileName, result.content)
        }
    }

    private fun fileIsOpened(fileName: String, content: InputStream) {
        content.close()
    }
}
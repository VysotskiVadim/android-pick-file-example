package dev.vadzimv.pickfile.example

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log

private const val TAG = "SafUtils"

val allSupportedDocumentsTypesToExtensions = mapOf(
    "application/msword" to ".doc",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to ".docx",
    "application/pdf" to ".pdf",
    "text/rtf" to ".rtf",
    "application/rtf" to ".rtf",
    "application/x-rtf" to ".rtf",
    "text/richtext" to ".rtf",
    "text/plain" to ".txt"
)
private val extensionsToTypes = allSupportedDocumentsTypesToExtensions.invert()

fun ContentResolver.queryFileName(uri: Uri): String? {
    val cursor: Cursor = query(uri, null, null, null, null) ?: return null
    val nameIndex: Int = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor.moveToFirst()
    val name: String = cursor.getString(nameIndex)
    cursor.close()
    return appendExtensionIfNeeded(name, uri)
}

private fun ContentResolver.appendExtensionIfNeeded(name: String, uri: Uri): String? {
    return if (hasKnownExtension(name)) {
        name
    } else {
        val type = getType(uri)
        if (type != null && allSupportedDocumentsTypesToExtensions.containsKey(type)) {
            return name + allSupportedDocumentsTypesToExtensions[type]
        }
        Log.e(TAG, "unknown file type: $type, for file: $name")
        name
    }
}

private fun hasKnownExtension(filename: String): Boolean {
    val lastDotPosition = filename.indexOfLast { it == '.' }
    if (lastDotPosition == -1) {
        return false
    }
    val extension = filename.substring(lastDotPosition)
    return extensionsToTypes.containsKey(extension)
}

private fun <K, V> Map<K, V>.invert(): Map<V, K> {
    val inverted = mutableMapOf<V, K>()
    for (item in this) {
        inverted[item.value] = item.key
    }
    return inverted
}
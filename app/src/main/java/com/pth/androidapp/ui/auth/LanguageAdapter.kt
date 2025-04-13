package com.pth.androidapp.ui.auth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.pth.androidapp.R
import com.pth.androidapp.common.LanguageManager

class LanguageAdapter(
    context: Context,
    private val languages: List<LanguageManager.LanguageItem>
) : ArrayAdapter<LanguageManager.LanguageItem>(context, R.layout.item_language, languages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_language, parent, false)

        val textViewLanguage = view.findViewById<TextView>(R.id.textViewLanguageName)
        val language = languages[position]
        textViewLanguage.text = language.displayName

        return view
    }
}
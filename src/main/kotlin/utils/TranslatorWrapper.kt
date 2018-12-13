package utils

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.common.collect.Lists
import java.io.FileInputStream

object TranslatorWrapper {


    lateinit var filePath: String

    private val credentials by lazy {
        GoogleCredentials.fromStream(FileInputStream(filePath))
            .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))

    }

    private val translate by lazy {
        TranslateOptions.newBuilder().setCredentials(credentials).build().service
    }

    var targetLanguage = "English -en"


    fun getLanguageCode(language: String): String {
        return language.substringAfter("-")
    }


    /**
     * takes a string and return a translated string
     * @param selectedText
     */
    fun translate(selectedText: String?): String {

        //Should not happen because we are disabling the plugin if nothing is selected. Added it for just an extra check
        if (selectedText.isNullOrEmpty()) {
            return ""
        }

        val translation = translate.translate(
            selectedText,
            Translate.TranslateOption.targetLanguage(getLanguageCode(targetLanguage))
        )

        return translation.translatedText!!
    }


    /**
     * return a list of language supported by google translator
     */
    fun getLanguageList(): List<Language> = translate.listSupportedLanguages()

}
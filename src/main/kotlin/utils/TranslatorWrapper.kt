package utils

import com.google.auth.oauth2.GoogleCredentials
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

    private var targetLanguage = "en"


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
            Translate.TranslateOption.targetLanguage(targetLanguage)
        )

        return translation.translatedText!!
    }


    fun changeTargetLanguageFromName(target: String) {
        val language = translate.listSupportedLanguages().find { it.name == target }
        language?.let {
            targetLanguage = language.code
        }

    }


    fun changeTargetLanguageFromCode(target: String) {
        targetLanguage = target
    }


    /**
     * return a list of language supported by google translator
     */
    fun getLanguageList() = translate.listSupportedLanguages()

}
package utils

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.common.collect.Lists
import java.io.FileInputStream

object TranslatorWrapper {

    lateinit var filePath: String
    private lateinit var translate: Translate
    var targetLanguage = DEFAULT_LANGUAGE

    fun getLanguageCode(language: String): String {
        return language.substringAfter("-")
    }

    fun initializeTranslator(notifyFunc: (String, String) -> Unit) {
        try {
            createTranslator()
        } catch (e: Exception) {
            notifyFunc.invoke(
                NOTIFICATION_TITLE,
                NOTIFICATION_MESSAGE
            )
        }
    }

    // this is a translation text

    private fun initializeTranslator() {
        try {
            createTranslator()
        } catch (e: Exception) {

        }
    }


    private fun createTranslator(){
        val credentials = GoogleCredentials.fromStream(FileInputStream(filePath))
            .createScoped(Lists.newArrayList(GOOGLE_CLOUD_URL))
        translate = TranslateOptions.newBuilder().setCredentials(credentials).build().service
    }


    /**
     * takes a string and return a translated string
     * @param selectedText
     */
    fun translateString(selectedText: String?): String {

        if (!TranslatorWrapper::translate.isInitialized) {
            initializeTranslator()
        }

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
    fun getLanguageList(notifyFunc: (String, String) -> Unit): List<Language> {
        if (!TranslatorWrapper::translate.isInitialized) {
            initializeTranslator(notifyFunc)
        }
        return translate.listSupportedLanguages()
    }

}
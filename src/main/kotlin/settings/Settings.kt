package settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import components.TranslatorComponents
import utils.TranslatorWrapper
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import com.google.cloud.translate.Language


class Settings(private val project: Project) : Configurable, DocumentListener {

    override fun changedUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun insertUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun removeUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun apply() {
        val config = TranslatorComponents.getInstance(project)
        config.configJsonPath = etJsonConfigFilePath.text.toString()
        config.preferedLanguage = etPreferredLanguage.text.toString()

        TranslatorWrapper.filePath = etJsonConfigFilePath.text.toString()
        TranslatorWrapper.targetLanguage = etPreferredLanguage.text.toString()
        modified = false
    }


    override fun createComponent(): JComponent? {

        etPreferredLanguage.document.addDocumentListener(this)
        etJsonConfigFilePath.document.addDocumentListener(this)


        val config = TranslatorComponents.getInstance(project)
        etJsonConfigFilePath.text = config.configJsonPath
        etPreferredLanguage.text = config.preferedLanguage


        return rootPanel
    }


    private var modified = false

    override fun isModified(): Boolean = modified

    override fun getDisplayName(): String = "Translator Plugin"


    lateinit var rootPanel: JPanel
    lateinit var etJsonConfigFilePath: JTextField
    lateinit var btnFileBrowse: JButton
    lateinit var cbLanguage: JComboBox<*>
    lateinit var etPreferredLanguage: JTextField

    init {
        btnFileBrowse.addActionListener { e ->

        }
        cbLanguage.addActionListener { e ->

        }

        cbLanguage.model = (DefaultComboBoxModel(TranslatorWrapper.getLanguageList().toTypedArray()))

        cbLanguage.selectedItem = TranslatorWrapper.getLanguageList().find { it.code == TranslatorWrapper.getLanguageCode(TranslatorWrapper.targetLanguage) }

        cbLanguage.addActionListener {
            val language =  (cbLanguage.selectedItem as Language)
            etPreferredLanguage.text = "${language.name} -${language.code}"
        }

    }
}

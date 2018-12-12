package settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import components.TranslatorComponents
import utils.TranslatorWrapper
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class TranslatorSettings(private val project: Project) : Configurable, DocumentListener {

    override fun changedUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun insertUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun removeUpdate(e: DocumentEvent?) {
        modified = true
    }

    private var modified = false

    override fun isModified(): Boolean = modified

    override fun getDisplayName(): String = "Translator Plugin"

    override fun apply() {
        val config = TranslatorComponents.getInstance(project)
        config.configJsonPath = etJsonConfigFilePath.text.toString()
        config.preferedLanguage = etPreferredLanguage.text.toString()

        TranslatorWrapper.filePath = etJsonConfigFilePath.text.toString()
        TranslatorWrapper.changeTargetLanguageFromCode(etPreferredLanguage.text.toString())
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

    lateinit var tvConfigFilePath: JLabel
    lateinit var etJsonConfigFilePath: JTextField
    lateinit var tvJsonFileDescription: JLabel
    lateinit var etPreferredLanguage: JTextField
    lateinit var rootPanel: JPanel


}

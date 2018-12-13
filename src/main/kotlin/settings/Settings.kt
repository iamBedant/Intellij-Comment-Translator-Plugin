package settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import components.TranslatorComponents
import utils.TranslatorWrapper
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import com.google.cloud.translate.Language
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView


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
        TranslatorWrapper.initializeTranslator()
        modified = false
    }

    override fun createComponent(): JComponent? {
        etPreferredLanguage.document.addDocumentListener(this)
        etJsonConfigFilePath.document.addDocumentListener(this)
        val config = TranslatorComponents.getInstance(project)
        etJsonConfigFilePath.text = config.configJsonPath
        etPreferredLanguage.text = config.preferedLanguage

        btnFileBrowse.addActionListener {
            val fileChooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
            val filter = FileNameExtensionFilter("Json File", "json")
            fileChooser.fileFilter = filter
            val returnValue = fileChooser.showOpenDialog(null)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val selectedFile = fileChooser.selectedFile
                etJsonConfigFilePath.text = selectedFile.absolutePath
                //re-initializing translator to populate combo-box.
                if(cbLanguage.itemCount==0){
                    TranslatorWrapper.filePath = selectedFile.absolutePath
                    TranslatorWrapper.initializeTranslator()
                }

                populateLanguageComboBox()
            }
        }
        if (!etJsonConfigFilePath.text.toString().isNullOrEmpty()) {
            populateLanguageComboBox()
        }

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

    private fun populateLanguageComboBox(){
        try {
            cbLanguage.model = (DefaultComboBoxModel(TranslatorWrapper.getLanguageList().toTypedArray()))
            if (!etPreferredLanguage.text.isNullOrEmpty()) {
                cbLanguage.selectedItem = TranslatorWrapper.getLanguageList()
                    .find { it.code == TranslatorWrapper.getLanguageCode(TranslatorWrapper.targetLanguage) }
            }
            cbLanguage.addActionListener {
                val language = (cbLanguage.selectedItem as Language)
                etPreferredLanguage.text = "${language.name} -${language.code}"
            }
        } catch (e: Exception) {
            //Show notification to update JSON config file path
        }
    }

}

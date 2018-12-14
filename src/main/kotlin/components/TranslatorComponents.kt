package components

import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import utils.DEFAULT_LANGUAGE
import utils.TranslatorWrapper
import java.io.Serializable



@State(name = "translatorConfiguration", storages = [Storage(value = "translatorConfiguration.xml")])
class TranslatorComponents :
    ApplicationComponent,
    Serializable,
    PersistentStateComponent<TranslatorComponents> {


    var configJsonPath: String = ""
    var preferedLanguage: String = DEFAULT_LANGUAGE

    override fun getState(): TranslatorComponents? = this

    override fun loadState(state: TranslatorComponents) = XmlSerializerUtil.copyBean(state, this)


    override fun initComponent() {
        super.initComponent()
        TranslatorWrapper.targetLanguage = preferedLanguage
        TranslatorWrapper.filePath = configJsonPath
    }

    companion object {
        fun getInstance(project: Project): TranslatorComponents =
            project.getComponent(TranslatorComponents::class.java)
    }
}
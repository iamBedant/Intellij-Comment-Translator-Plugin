package utils

import org.junit.Test



class TranslatorWrapperTest {

    @Test
    fun `getLanguageCode function works properly`(){
        assert(TranslatorWrapper.getLanguageCode("English -en")=="en")
    }

}
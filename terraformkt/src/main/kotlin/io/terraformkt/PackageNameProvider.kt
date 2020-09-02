package io.terraformkt

import io.terraformkt.utils.Json
import io.terraformkt.utils.Text
import java.io.File

class PackageNameProvider(private val provider: String) {
    private val mappings = getMappings()

    companion object {
        private const val PACKAGE_PREFIX = "io.terraformkt"
        private const val PATH_PREFIX = "io/terraformkt"
        private const val RESOURCES_DIRECTORY_NAME = "resource_schemas"
        private const val DATA_DIRECTORY_NAME = "data_source_schemas"
    }

    fun getClassFilePath(resourceType: TerraformGenerator.ResourceType, className: String): String {
        return "$PATH_PREFIX/$provider/${getDirectoryName(resourceType)}/${getClassPackageName(className)}/$className.kt"
    }

    fun getPackageName(resourceType: TerraformGenerator.ResourceType, className: String): String {
        return "$PACKAGE_PREFIX.$provider.${getDirectoryName(resourceType)}.${getClassPackageName(className)}"
    }

    private fun getMappings(): Map<String, String>? {
        val mappingFile = File("src/main/resources/package_mapping.json")
        if (!mappingFile.exists()) {
            return null
        }
        return Json.parse<Map<String, String>>(mappingFile.readText())
    }

    private fun getDirectoryName(resourceType: TerraformGenerator.ResourceType): String {
        return when (resourceType) {
            TerraformGenerator.ResourceType.RESOURCE -> RESOURCES_DIRECTORY_NAME
            TerraformGenerator.ResourceType.DATA -> DATA_DIRECTORY_NAME
        }
    }

    private fun getClassPackageName(className: String): String {
        if (mappings != null) {
            val packageNameEntry = mappings.entries.find { entry -> className.startsWith(entry.key) }
            if (packageNameEntry != null) {
                return packageNameEntry.value
            }
        }
        return getFirstWordCamelCase(className).toLowerCase()
    }

    private fun getFirstWordCamelCase(word: String): String {
        return Text.decamelize(word)[0]
    }
}

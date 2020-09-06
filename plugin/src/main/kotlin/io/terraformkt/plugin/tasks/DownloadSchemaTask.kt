package io.terraformkt.plugin.tasks

import io.terraformkt.plugin.terraformKt
import io.terraformkt.utils.CommandLine
import io.terraformkt.utils.myResolve
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

open class DownloadSchemaTask : DefaultTask() {
    init {
        group = "terraformkt"
        outputs.upToDateWhen { false }
    }

    @get:InputDirectory
    var root: File = terraformKt.downLoadTerraformPath!!.myResolve()

    @TaskAction
    fun execOperation() {
        val terraformPath = root.resolve("terraform").absolutePath
        createConfigFile(terraformKt.tfProvider ?: error("tfProvider is null"), terraformKt.schemaVersion ?: error("schemaVersion is null"))

        CommandLine.executeOrFail(terraformPath, listOf("init"), root, redirectStdout = true, redirectErr = true)
        CommandLine.executeOrFailToFile(terraformPath, listOf("providers", "schema", "-json"), root,
            root.resolve("schema.json"), redirectErr = true)

    }

    private fun createConfigFile(tfProvider: String, schemaVersion: String) {
        val configFile = terraformKt.downLoadTerraformPath!!.myResolve().resolve("config.tf")
        configFile.createNewFile()
        configFile.writeText("""
        |provider "$tfProvider" {
        |    version = "$schemaVersion"
        |}
        """.trimMargin())
    }
}

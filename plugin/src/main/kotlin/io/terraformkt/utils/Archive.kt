package io.terraformkt.utils

import org.codehaus.plexus.archiver.AbstractUnArchiver
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver
import org.codehaus.plexus.archiver.zip.ZipUnArchiver
import org.codehaus.plexus.logging.console.ConsoleLogger
import java.io.File

enum class Archive(val extension: String) {
    ZIP("zip") {
        override fun getUnarchiver(from: File) = ZipUnArchiver(from)
    };

    companion object {
        private const val plexusErrorLoggerLevel = 5
    }

    internal abstract fun getUnarchiver(from: File): AbstractUnArchiver

    internal fun unarchive(from: File, to: File) {
        to.mkdirs()
        getUnarchiver(from).apply {
            enableLogging(ConsoleLogger(plexusErrorLoggerLevel, "Archive"))
            sourceFile = from
            destDirectory = to
        }.extract()
    }
}

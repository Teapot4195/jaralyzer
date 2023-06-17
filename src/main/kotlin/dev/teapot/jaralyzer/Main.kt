package dev.teapot.jaralyzer

import dev.teapot.jaralyzer.Util.Info
import dev.teapot.jaralyzer.Util.Warn
import dev.teapot.jaralyzer.classfile.ClassFileReader
import software.coley.llzip.ZipIO
import software.coley.llzip.format.model.ZipArchive
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.channels.Pipe
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path

fun main(args: Array<String>) {
    if (args.size == 0) {
        System.out.println("Usage: java -jar mainjar.jar <jarpath>+")
    }

    val corePoolSize = 4
    val maximumPoolSize = Runtime.getRuntime().availableProcessors()
    val keepAliveTime = 100L
    val workQueue = SynchronousQueue<Runnable>()
    val workerPool: ExecutorService = ThreadPoolExecutor(
        corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue
    )

    for (jar in args) {
        val archive: ZipArchive = ZipIO.readStandard(Path(jar))

        for (file in archive.localFiles) {
            workerPool.submit {
                Info("Begin read: $file")
                val input: PipedInputStream = PipedInputStream()
                val output: PipedOutputStream = PipedOutputStream(input)
                val buf: ByteArray = ByteArray(256)
                file.fileData.transferTo(output, buf)

                val read: ClassFileReader = ClassFileReader(input)

                Info ("End read: $file")
                read.flags.print()
            }
        }
    }
}
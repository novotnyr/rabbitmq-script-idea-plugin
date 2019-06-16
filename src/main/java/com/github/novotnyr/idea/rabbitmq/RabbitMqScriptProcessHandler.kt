package com.github.novotnyr.idea.rabbitmq

import com.github.novotnyr.idea.rabbitmq.console.GetMessageOutputSerializer
import com.github.novotnyr.idea.rabbitmq.console.PublishToExchangeOutputSerializer
import com.github.novotnyr.idea.rabbitmq.console.StdOut
import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.RabbitMqAccessDeniedException
import com.github.novotnyr.scotch.RabbitMqConnectionException
import com.github.novotnyr.scotch.command.GetMessage
import com.github.novotnyr.scotch.command.PublishToExchange
import com.github.novotnyr.scotch.command.api.PublishToExchangeResponse
import com.github.novotnyr.scotch.command.script.ExecuteScript
import com.github.novotnyr.scotch.command.script.StdErr
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.psi.PsiFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.io.StringWriter

class RabbitMqScriptProcessHandler(
        private val scriptPsiFile: PsiFile,
        private val scriptIndex: Int,
        private val rabbitConfiguration: RabbitConfiguration?
) : CallableProcessHandler() {

    private val isValidConfiguration: Boolean
        get() {
            if (this.rabbitConfiguration == null) {
                notifyTextAvailable("No RabbitMQ configuration is provided. Please configure it in a script file or create a profile", ProcessOutputTypes.STDERR)
                notifyProcessTerminated(1)
                return false
            }
            return true
        }

    override fun doCall(): Void? {
        if (!isValidConfiguration) {
            return NOTHING
        }
        val scriptFile = this.scriptPsiFile.virtualFile.path
        val executeScript = ExecuteScript(this.rabbitConfiguration, scriptFile)
        executeScript.includedCommandIndices = listOf(this.scriptIndex)
        val stdErr = object : StdErr {
            override fun println(message: String) {
                val text = StringBuilder(message)
                if (!message.endsWith(System.lineSeparator())) {
                    text.append(System.lineSeparator())
                }
                notifyTextAvailable(text.toString(), ProcessOutputTypes.STDERR)
            }
        }
        val stdOut = StdOut { message ->
            val text = StringBuilder(message)
            if (!message.endsWith(System.lineSeparator())) {
                text.append(System.lineSeparator())
            }
            notifyTextAvailable(text.toString(), ProcessOutputTypes.STDOUT)
        }
        executeScript.stdErr = stdErr
        configureOutputSerializers(executeScript, stdOut, stdErr)
        run(executeScript)
        return NOTHING
    }

    private fun run(executeScript: ExecuteScript) {
            GlobalScope.launch {
                try {
                    executeScript.run()
                    notifyTextAvailable("RabbitMQ script completed", ProcessOutputTypes.SYSTEM)
                    notifyProcessTerminated(0)
                } catch (e: RabbitMqConnectionException) {
                    notifyTextAvailable(e.message!!, ProcessOutputTypes.STDERR)
                } catch (e: RabbitMqAccessDeniedException) {
                    notifyTextAvailable("Access denied to RabbitMQ broker. Please verify the credentials", ProcessOutputTypes.STDERR)
                    notifyProcessTerminated(3)
                }
            }
    }

    private fun configureOutputSerializers(executeScript: ExecuteScript, stdOut: StdOut, stdErr: StdErr) {
        executeScript.setOutputSerializer(GetMessage::class.java, GetMessageOutputSerializer(stdOut))
        executeScript.setOutputSerializer<PublishToExchange, PublishToExchangeResponse>(PublishToExchange::class.java, PublishToExchangeOutputSerializer(stdOut, stdErr))
    }

    override fun handleException(e: Exception): Void? {
        val out = StringWriter()
        e.printStackTrace(PrintWriter(out))
        notifyTextAvailable(out.toString(), ProcessOutputTypes.STDERR)
        notifyProcessTerminated(1)
        return NOTHING
    }


}

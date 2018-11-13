package nl.dorost.flow.actions

import com.github.kittinunf.fuel.Fuel
import nl.dorost.flow.core.Action
import org.slf4j.LoggerFactory
import java.lang.RuntimeException

val LOG = LoggerFactory.getLogger("ElementaryBlocks")


val elementaryActions = mutableMapOf<String, (action: Action) -> Map<String, Any>>(
    "log" to { action: Action ->
        LOG.info("LOG: Action id '${action.id}': ${action.name}. Input Value: ${action.input}, Params: ${action.params}")
        action.input
    },
    "constant" to { action: Action ->
        LOG.debug("Action id '${action.id}': ${action.name}. Input Value: ${action.input}, Params: ${action.params}")
        val constValue = action.params["const"] ?: throw UnsatisfiedParamsException("Parameter const not found!")
        mapOf("value" to constValue)
    },
    "http-post" to { action: Action ->
        val url = action.params["url"] ?: throw UnsatisfiedParamsException("Parameter url not found!")
        val contentType = action.params["CONTENT_TYPE"]?: "application/json"
        val accept = action.params["Accept"]?: "application/json"
        val body = action.input["body"]?: UnsatisfiedInputException("Missing body in the input!")

        val result = Fuel
            .post(url)
            .header("ContentType" to contentType, "Accept" to accept)
            .body(body as String)
            .responseString()
        mapOf("response" to String(result.second.data))
    },
    "http-get" to { action: Action ->
        val url = action.params["url"] ?: throw UnsatisfiedParamsException("Parameter url not found!")
        val contentType = action.params["CONTENT_TYPE"]?: "application/json"
        val accept = action.params["Accept"]?: "application/json"
        val result = Fuel.get(url)
            .header("ContentType" to contentType, "Accept" to accept)
            .responseString()
        mapOf("response" to String(result.second.data))
    },
    "json-in" to { action: Action ->
        val value = action.params["value"]?: throw UnsatisfiedParamsException("Value not found!")
        mapOf("body" to value)
    }
)

class UnsatisfiedParamsException(msg: String): RuntimeException(msg)
class UnsatisfiedInputException(msg: String): RuntimeException(msg)
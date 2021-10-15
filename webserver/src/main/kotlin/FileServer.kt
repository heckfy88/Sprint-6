import ru.sber.filesystem.VFilesystem
import ru.sber.filesystem.VPath
import java.io.IOException
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
class FileServer {

    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs     A proxy filesystem to serve files from. See the VFilesystem
     *               class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    @Throws(IOException::class)
    fun run(socket: ServerSocket, fs: VFilesystem) {
        socket.use {

            while (true) {
                val sckt = it.accept()
                handle(sckt, fs)

            }
        }
    }

    private fun handle(socket: Socket, fs: VFilesystem) {
        socket.use { s ->

            val reader = s.getInputStream().bufferedReader()
            val clientRequest = reader.readLine()

            val writer = PrintWriter(s.getOutputStream())
            writer.write(getResponse(clientRequest, fs))
            writer.flush()
        }
    }

    private fun getResponse(clientRequest: String, fs: VFilesystem): String {
        val request = clientRequest.split(" ").first()

        if (request == "GET") {
            val path = VPath(clientRequest.split(" ")[1])
            val file = fs.readFile(path)
            if (file != null) {
                return getOkResponse(file)
            }
        }
        return getNotFoundResponse()
    }


    private fun getOkResponse(file: String): String {
        return "HTTP/1.0 200 OK\r\n" +
                "Server: FileServer\n\n" +
                "\r\n" +
                "$file\r\n"
    }

    private fun getNotFoundResponse(): String {
        return "HTTP/1.0 404 Not Found\r\n" +
                "Server: FileServer\n\n" +
                "\r\n"
    }

}
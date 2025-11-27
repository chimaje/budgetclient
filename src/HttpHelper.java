
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpHelper {

    public static String sendGet(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            int rc = con.getResponseCode();
            InputStream is = rc >= 400 ? con.getErrorStream() : con.getInputStream();
            if (is == null) return "HTTP " + rc;
            return read(is);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String sendPost(String urlStr, String jsonBody) {
        return sendWithBody("POST", urlStr, jsonBody);
    }

    public static String sendPut(String urlStr, String jsonBody) {
        return sendWithBody("PUT", urlStr, jsonBody);
    }

    public static String sendDelete(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            int rc = con.getResponseCode();
            InputStream is = rc >= 400 ? con.getErrorStream() : con.getInputStream();
            return is == null ? "HTTP " + rc : read(is);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String sendWithBody(String method, String urlStr, String jsonBody) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            if (jsonBody != null && !jsonBody.isEmpty()) {
                try (OutputStream os = con.getOutputStream()) {
                    os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                }
            }

            int rc = con.getResponseCode();
            InputStream is = rc >= 400 ? con.getErrorStream() : con.getInputStream();
            return is == null ? "HTTP " + rc : read(is);

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String read(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            return sb.toString().trim();
        }
    }
}

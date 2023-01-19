import com.intellij.openapi.util.AsyncResult;
import com.intellij.util.concurrency.FutureResult;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class MemeProcessor {

    public void getTopMemes() {

        try {

            URL url = new URL("https://api.imgflip.com/get_memes");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            System.out.println("Response code: " + responseCode);


            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }

            scanner.close();

            JSONParser parser = new JSONParser();

            JSONObject countryData = (JSONObject) parser.parse(String.valueOf(informationString));

            var memesArray = (JSONArray) ((JSONObject) countryData.get("data")).get("memes");

            Map<String, String> templateIds = new HashMap<>();
            for (int i = 0; i < 100; i++) {

                System.out.println((i + 1) + "/" + "100");
                JSONObject elemJSONObject = (JSONObject) memesArray.get(i);

                URL imgUrl = new URL((String) elemJSONObject.get("url"));
                InputStream in = new BufferedInputStream(imgUrl.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }

                out.close();
                in.close();

                String fileName = (String) elemJSONObject.get("name");
                String id = (String) elemJSONObject.get("id");

                templateIds.put(id, fileName);

            }

            String templatesInfoPath = "C:\\Users\\HP\\OneDrive\\Desktop\\labs\\sem5\\tinka3\\src\\main\\resources\\templates.info";
            FileOutputStream fos = new FileOutputStream(templatesInfoPath);
            for (var elem : templateIds.entrySet()) {
                String row = elem.getKey() + "-" + elem.getValue() + "\n";
                fos.write(row.getBytes(StandardCharsets.UTF_8));
            }

            fos.close();

        } catch (Exception e) {
            throw new MemeProcessingException("Exception occurred due getting memes");
        }
    }

    public String generateMeme(String templateId, String text0, String text1) {

        String filePath = "";
        try {
            File fileCred = new File("C:\\Users\\HP\\OneDrive\\Desktop\\labs\\sem5\\tinka3\\src\\main\\resources\\imgflip.cred");
            Scanner scanner = new Scanner(fileCred);

            String username = scanner.nextLine();
            String password = scanner.nextLine();
            scanner.close();

            URL url = new URL("https://api.imgflip.com/caption_image");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setDoInput(true);
            Map<String, String> arguments = new HashMap<>();

            arguments.put("username", username);
            arguments.put("password", password);

            arguments.put("template_id", templateId);
            arguments.put("text0", text0);
            arguments.put("text1", text1);

            StringJoiner sj = new StringJoiner("&");
            for (Map.Entry<String, String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            System.out.println(sj);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(out);
            InputStream is = http.getInputStream();
            String text = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            System.out.println(text);

            JSONParser parser = new JSONParser();

            JSONObject imgJson = (JSONObject) parser.parse(text);


            URL imgUrl = new URL((String) ((JSONObject) imgJson.get("data")).get("url"));
            InputStream in = new BufferedInputStream(imgUrl.openStream());
            ByteArrayOutputStream outt = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = in.read(buf))) {
                outt.write(buf, 0, n);
            }

            outt.close();
            in.close();
            byte[] response = outt.toByteArray();

            filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\" + "aboba" + ".jpg";
            File templateFile = new File("C:\\Users\\HP\\OneDrive\\Desktop\\labs\\sem5\\tinka3\\src\\main\\resources\\templates.info");
            scanner = new Scanner(templateFile);
            while (scanner.hasNextLine()) {
                String[] row = (scanner.nextLine().split("-"));
                if (Integer.parseInt(row[0]) == Integer.parseInt(templateId)) {
                    filePath = "C:\\Users\\HP\\OneDrive\\Desktop\\labs\\sem5\\tinka3\\src\\main\\resources\\" + row[1] + ".jpg";
                    break;
                }
            }
            scanner.close();

            FileOutputStream foss = new FileOutputStream(filePath);
            foss.write(response);
            foss.close();
        } catch (Exception e) {
            throw new MemeProcessingException("Exception occurred due generating memes");
        }

        return filePath;
    }
}

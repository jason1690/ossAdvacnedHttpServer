import endpoints.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Config {

    public static Map<String,String> mimeTypes = new HashMap<>();
    public static ArrayList<String> supportedRequestMethods = new ArrayList<String>();
    public static Map<String,String[]> fileMap = new ConcurrentHashMap<>();
    public static Map<String, Endpoint> endpointMap = new HashMap<>();
    public static Map<String,String> switches = new HashMap<String,String>();

    public static int mainServerPort=3000;

    public static void parseArgs(String[] args){
        for(int i=0;i<args.length;i++){
            if(args[i].startsWith("-")){
                if(!(i+1>=args.length)&&!(args[i+1].startsWith("-")))
                    switches.put(args[i],args[++i]);
                else
                    switches.put(args[i],"");
            }
        }
        if(switches.containsKey("--port"))
            mainServerPort = Integer.valueOf(switches.get("--port"));
        if(switches.containsKey("-p"))
            mainServerPort = Integer.valueOf(switches.get("-p"));
    }

    public static void loadAllDefaults(){
        mimeTypes.put(".aac","audio/aac");
        mimeTypes.put(".abw","application/x-abiword");
        mimeTypes.put(".arc","application/x-freearc");
        mimeTypes.put(".avi","video/x-msvideo");
        mimeTypes.put(".azw","application/vnd.amazon.ebook");
        mimeTypes.put(".bin","application/octet-stream");
        mimeTypes.put(".bmp","image/bmp");
        mimeTypes.put(".bz","application/x-bzip");
        mimeTypes.put(".bz2","application/x-bzip2");
        mimeTypes.put(".csh","application/x-csh");
        mimeTypes.put(".css","text/css");
        mimeTypes.put(".csv","text/csv");
        mimeTypes.put(".doc","application/msword");
        mimeTypes.put(".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypes.put(".eot","application/vnd.ms-fontobject");
        mimeTypes.put(".epub","application/epub+zip");
        mimeTypes.put(".gz","application/gzip");
        mimeTypes.put(".gif","image/gif");
        mimeTypes.put(".html","text/html");
        mimeTypes.put(".htm","text/html");
        mimeTypes.put(".ico","image/vnd.microsoft.icon");
        mimeTypes.put(".ics","text/calendar");
        mimeTypes.put(".jar","application/java-archive");
        mimeTypes.put(".jpeg","image/jpeg");
        mimeTypes.put(".jpg","image/jpeg");
        mimeTypes.put(".js"," text/javascript, per the following specifications:  https://html.spec.whatwg.org/multipage/#scriptingLanguages https://html.spec.whatwg.org/multipage/#dependencies:willful-violation https://datatracker.ietf.org/doc/draft-ietf-dispatch-javascript-mjs/  ");
        mimeTypes.put(".json","application/json");
        mimeTypes.put(".jsonld","application/ld+json");
        mimeTypes.put(".mid","audio/midi");
        mimeTypes.put(".midi","audio/midi");
        mimeTypes.put(".mjs","text/javascript");
        mimeTypes.put(".mp3","audio/mpeg");
        mimeTypes.put(".mpeg","video/mpeg");
        mimeTypes.put(".mpkg","application/vnd.apple.installer+xml");
        mimeTypes.put(".odp","application/vnd.oasis.opendocument.presentation");
        mimeTypes.put(".ods","application/vnd.oasis.opendocument.spreadsheet");
        mimeTypes.put(".odt","application/vnd.oasis.opendocument.text");
        mimeTypes.put(".oga","audio/ogg");
        mimeTypes.put(".ogv","video/ogg");
        mimeTypes.put(".ogx","application/ogg");
        mimeTypes.put(".opus","audio/opus");
        mimeTypes.put(".otf","font/otf");
        mimeTypes.put(".png","image/png");
        mimeTypes.put(".pdf","application/pdf");
        mimeTypes.put(".php","application/x-httpd-php");
        mimeTypes.put(".ppt","application/vnd.ms-powerpoint");
        mimeTypes.put(".pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
        mimeTypes.put(".rar","application/vnd.rar");
        mimeTypes.put(".rtf","application/rtf");
        mimeTypes.put(".sh","application/x-sh");
        mimeTypes.put(".svg","image/svg+xml");
        mimeTypes.put(".swf","application/x-shockwave-flash");
        mimeTypes.put(".tar","application/x-tar");
        mimeTypes.put(".tif","image/tiff");
        mimeTypes.put(".tiff","image/tiff");
        mimeTypes.put(".ts","video/mp2t");
        mimeTypes.put(".ttf","font/ttf");
        mimeTypes.put(".txt","text/plain");
        mimeTypes.put(".vsd","application/vnd.visio");
        mimeTypes.put(".wav","audio/wav");
        mimeTypes.put(".weba","audio/webm");
        mimeTypes.put(".webm","video/webm");
        mimeTypes.put(".webp","image/webp");
        mimeTypes.put(".woff","font/woff");
        mimeTypes.put(".woff2","font/woff2");
        mimeTypes.put(".xhtml","application/xhtml+xml");
        mimeTypes.put(".xls","application/vnd.ms-excel");
        mimeTypes.put(".xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypes.put(".xml","text/xml");
        mimeTypes.put(".xul","application/vnd.mozilla.xul+xml");
        mimeTypes.put(".zip","application/zip");
        mimeTypes.put(".3gp","video/3gpp");
        mimeTypes.put(".3g2","video/3gpp2");
        mimeTypes.put(".7z","application/x-7z-compressed");
        supportedRequestMethods.add("GET");
        supportedRequestMethods.add("POST");
        loadFileTable();
        registerEndpoint(new RegisterPage());
        registerEndpoint(new LoginPage());
        registerEndpoint(new UserPage());
    }

    public static String getType(String fileExtension){
        if(mimeTypes.get(fileExtension)==null)
            return "application/octet-stream";
        return mimeTypes.get(fileExtension);
    }

    public static synchronized void loadFileTable(){
        Map<String,String[]> newMap = new ConcurrentHashMap<>();
        if(!switches.containsKey("--no-autoload"))
            loadGeneratedMap(newMap);
        try{
            BufferedReader reader = new BufferedReader(new FileReader("filetable"));
            String line;
            while((line= reader.readLine())!=null){
                if(line.equals(""))
                    continue;
                String[] split = line.split(":");
                if(split.length<3) {
                    System.err.println("failed to parse filetable line " + line);
                    continue;
                }
                String[] typedFile = new String[]{"static/"+split[1].trim(),split[2].trim()};
                newMap.put(split[0].trim(), typedFile);
            }
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Filetable not Loaded");
            return;
        }
        fileMap=newMap;
    }

    public static void loadGeneratedMap(Map<String,String[]> map){
        File dir = new File("static");
        if(!dir.exists()||!dir.isDirectory())
            return;
        loadAllFiles(map,new File("static"));
    }

    public static void loadAllFiles(Map<String,String[]> map,File dir){
        if(!dir.exists())
            return;
        if(!dir.isDirectory()){
            addFile(map,dir.getPath());
            return;
        }
        for(File f : dir.listFiles()){
            loadAllFiles(map,f);
        }
    }

    public static void addFile(Map<String,String[]> map, String filename){
        System.out.println(filename);
        String webName = filename.substring(6);
        String localName = "static"+webName;
        String type;
        if(!filename.contains("."))
            type="application/octet-stream";
        else {
            String[] splitname = filename.split("\\.");
            type = getType("."+splitname[splitname.length-1]);
        }
        map.put(webName, new String[]{localName,type});
    }

    public static void registerEndpoint(Endpoint endpoint){
        endpointMap.put(endpoint.getLocation(),endpoint);
    }
}

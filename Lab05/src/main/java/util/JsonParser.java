package util;

import repo.Resource;

import java.io.*;

public class JsonParser {
    private BufferedReader reader;
    private String path;

    public JsonParser(String path){
        try {
            this.path = path;
            this.reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource readResource(){

        String line;
        try{
            line = reader.readLine();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        if(line == null) return null;
        return new Resource(getField(line,"id"), getField(line,"title"), getField(line,"location"), getField(line, "year"), getField(line,"author"));
    }

    private String getField(String line, String key){
        String search = "\"" + key + "\":\"";
        int start = line.indexOf(search) + search.length();
        int end = line.indexOf("\"", start);
        return line.substring(start, end);
    }

}

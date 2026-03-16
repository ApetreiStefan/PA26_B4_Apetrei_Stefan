package repo;

import util.JsonParser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositoryActions {
    public static void printAll(Repository repository){
        for(Resource resource : repository.getResources()){
            System.out.println(resource);
        }
    }
    public static List<Resource> generateResourceListFromResFile(String path){
        JsonParser jsonParser = new JsonParser(path);
        List<Resource> resources = new ArrayList<>();
        Resource resource = jsonParser.readResource();
        while(resource != null){
            resources.add(resource);
            resource = jsonParser.readResource();
        }

        return resources;
    }

    public static void openFile(Resource resource){
        if(resource == null){
            System.out.println("resource is null");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try{
            desktop.open(new File(resource.getLocation()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

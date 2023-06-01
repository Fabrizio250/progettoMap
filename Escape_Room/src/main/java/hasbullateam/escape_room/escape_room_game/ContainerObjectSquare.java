/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hasbullateam.escape_room.escape_room_game;

import hasbullateam.escape_room.type.Cord;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author giuse
 */
public class ContainerObjectSquare extends ObjectSquare{
        
    public List<ObjectSquare> objectList;

    String brief;

    public ContainerObjectSquare(JSONObject jsonObj) {
        super(
            jsonObj.getString("name"),
            new Cord(jsonObj.getJSONObject("position").getInt("x"),
                     jsonObj.getJSONObject("position").getInt("y")),
    jsonObj.getString("pathImage"),
   false, true
        );
        this.brief = jsonObj.getString("brief");

        this.objectList = new ArrayList<>();

        JSONArray objArray = jsonObj.getJSONArray("objects");

        for(int i=0; i<objArray.length(); i++){
            objectList.add( new ObjectSquare(objArray.getJSONObject(i)) );
        }
    }


    public List<String> getObjectsString(){
        List<String> objStr = new ArrayList<>(objectList.size());

        for(ObjectSquare obj: this.objectList){
            objStr.add(obj.name);
        }
        return objStr;
    }

    public String getBrief(){
        return this.brief;
    }
    
    public ObjectSquare getFromName(String name){
        
        
        for(ObjectSquare ele: this.objectList){
            if(ele.name.equals(name)){
                
               return ele;
            }
        }
        return null;
    }
    
    public void removeFromName(String name){
        
        for(int i=0; i<this.objectList.size(); i++){
            
            if(this.objectList.get(i).name.equalsIgnoreCase(name)){
                
                this.objectList.remove(i);
                return;
            }
                
        }
    }




}
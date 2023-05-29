
package hasbullateam.escape_room.escape_room_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author giuse
 */
public class MultipleChoiceDialog extends TextDialog{
    
    int selectedIndx=0;
    List<String> choices;
    String brief;

    public MultipleChoiceDialog(JPanel parent) {
        super(parent);
        choices = new ArrayList<>();
    
    }
    
    public void assembleText(){
        this.text = "";
        this.text = "<p>"+this.brief+"</p>";
        for(int i=0; i<this.choices.size(); i++){
            this.text += "<p><pre>";
            if(selectedIndx == i){
                this.text += " > <em>";
            }else{
                this.text += "   ";
            }
            this.text += this.choices.get(i);
            if(selectedIndx == i){
                this.text+="</em>";
            }
            this.text += "</pre></p>";
        }
    }
    
    public void setBrief(String brief){
        
        this.brief = brief;
    }
    
    public void setChoices(String... choices){
        this.choices.addAll(Arrays.asList(choices));
        
    }
    
    public void select(int indx){
        if(indx>=0 && indx<this.choices.size()){
            this.selectedIndx = indx;
            this.assembleText();
            this.reWriteText(false);
        }
        
    }
    
    public String getChoice(){
        return this.choices.get(this.selectedIndx);
    }
    
    
    
}

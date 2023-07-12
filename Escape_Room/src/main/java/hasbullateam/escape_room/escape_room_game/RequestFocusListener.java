
package hasbullateam.escape_room.escape_room_game;

import javax.swing.JComponent;

/**
 *
 * @author giuse
 */
public class RequestFocusListener implements javax.swing.event.AncestorListener {
    @Override
    public void ancestorAdded(javax.swing.event.AncestorEvent e) {
        JComponent component = e.getComponent();
        component.requestFocusInWindow();
        component.removeAncestorListener(this);
    }

    @Override
    public void ancestorMoved(javax.swing.event.AncestorEvent e) {}

    @Override
    public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
}
    
    
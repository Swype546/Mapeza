package be.ecam.mapeza.mapeza;

/**
 * Created by Thibaut on 26/03/2017.
 */

public class PlaceType
{
    private String name = null;
    private String APIcode = null;
    private boolean selected = false;

    public PlaceType(String name, boolean selected)
    {
        this.name = name;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

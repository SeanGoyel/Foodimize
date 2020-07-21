package model;

//The effects, how it made you feel and what type of emotion was it
public class Effect {
    private String sensationType;
    private String effectDescription;

    public Effect(String effectDescription, String sensationType) {
        this.effectDescription = effectDescription;
        this.sensationType = sensationType;

    }

    public String getEffectDescription() {
        return effectDescription;
    }

    public String getSensationType() {
        return sensationType;
    }
}

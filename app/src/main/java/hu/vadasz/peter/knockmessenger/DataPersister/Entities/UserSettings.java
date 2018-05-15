package hu.vadasz.peter.knockmessenger.DataPersister.Entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Entity to store the application's basic settings. This class is planned for later use!
 */

@AllArgsConstructor
@Entity(generateConstructors = false, generateGettersSetters = false)
public class UserSettings {

    public static final String PAUSE_TIME = "pauseTime";
    public static final String MEASURE_TIME = "shortUnitLength";

    @Id
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Getter
    @Setter
    private String setting;

    @NotNull
    @Getter
    @Setter
    private String value;

    public UserSettings() {
    }

}

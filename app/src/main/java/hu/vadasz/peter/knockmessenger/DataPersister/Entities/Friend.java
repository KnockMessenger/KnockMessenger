package hu.vadasz.peter.knockmessenger.DataPersister.Entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * This class is used to store contacts in a different table.
 */

@AllArgsConstructor
@Entity(generateConstructors = false, generateGettersSetters = false)
public class Friend {

    @Id
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String tel;

    public Friend() {
    }

    public Friend(User user) {
        name = user.getName();
        tel = user.getTelephone();
    }

    public Friend(Friend other) {
        id = other.id;
        name = other.name;
        tel = other.tel;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else if (this == other) {
            return true;
        }

        return ((Friend) other).tel.equals(this.tel);
    }
}

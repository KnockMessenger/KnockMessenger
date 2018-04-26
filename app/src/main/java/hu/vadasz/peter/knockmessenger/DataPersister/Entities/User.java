package hu.vadasz.peter.knockmessenger.DataPersister.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * This class stores user data.
 */

@IgnoreExtraProperties
@AllArgsConstructor
@Entity(generateGettersSetters = false, generateConstructors = false)
public class User implements Parcelable {

    @Getter
    @Setter
    @Id
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String telephone;

    @Getter
    @Setter
    private String password = "";

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(telephone);
        dest.writeString(password);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        telephone = in.readString();
        password = in.readString();
    }
}

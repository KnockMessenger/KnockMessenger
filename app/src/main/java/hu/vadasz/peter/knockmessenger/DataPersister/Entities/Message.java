package hu.vadasz.peter.knockmessenger.DataPersister.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.joda.time.DateTime;

import java.security.spec.PSSParameterSpec;
import java.util.Date;

/**
 *
 */

@Entity(generateGettersSetters = false, generateConstructors = false)
@AllArgsConstructor
public class Message implements Parcelable {

    public enum MessageType {IN, OUT}

    public static final boolean SHOW_TIMESTAMP = true;

    @Id
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String key;

    @Getter
    @Setter
    private String fromTelephone;

    @Getter
    @Setter
    private String toTelephone;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    //@Convert(converter = JodaDateTimeConverter.class, columnType = Long.class)
    private Long dateTime;

    @Getter
    @Setter
    @Convert(converter = MessageTypeConverter.class, columnType = String.class)
    private MessageType messageType;

    @Getter
    @Setter
    private Boolean deleted;

    @Getter
    @Setter
    @Transient
    private boolean showTimeStamp;

    public DateTime convertDateTimeAsToTimestamp() {
        return new DateTime(dateTime);
    }

    public void setDateTimeFromTimestamp(DateTime dateTime) {
        this.dateTime = dateTime.getMillis();
    }

    public Message() {
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        } else if (this == other || this.key.equals(((Message) other).key)) {
            return true;
        }

        return false;
    }

    public static class JodaDateTimeConverter implements PropertyConverter<DateTime, Long> {

        @Override
        public DateTime convertToEntityProperty(Long databaseValue) {
            return new DateTime(databaseValue);
        }

        @Override
        public Long convertToDatabaseValue(DateTime entityProperty) {
            return entityProperty.getMillis();
        }
    }

    public static class MessageTypeConverter implements PropertyConverter<MessageType, String> {

        @Override
        public MessageType convertToEntityProperty(String databaseValue) {
            return MessageType.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(MessageType entityProperty) {
            return entityProperty.toString();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(fromTelephone);
        dest.writeString(toTelephone);
        dest.writeString(key);
        dest.writeString(messageType.toString());
        dest.writeString(message);
        dest.writeLong(dateTime);
    }

    public static final Parcelable.Creator<Message> CREATOR
            = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public Message(Parcel in) {
        id = in.readLong();
        message = in.readString();
        fromTelephone = in.readString();
        toTelephone = in.readString();
        key = in.readString();
        messageType = MessageType.valueOf(in.readString());
        dateTime = in.readLong();
    }
}

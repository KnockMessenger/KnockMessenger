package hu.vadasz.peter.knockmessenger.DataPersister.JSON;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import hu.vadasz.peter.knockmessenger.DataPersister.Exceptions.JsonFileSavingException;
import hu.vadasz.peter.morsecodedecoder.Code.Code;

/**
 * This class is responsible for parsing data to json format and save/load from file.
 */

public class JsonParser {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final String FILE_SAVING_ERROR = "Code table can not be saved!";

    /// CONSTANTS -- END

    private Gson gson;
    private Context context;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public JsonParser(Context context) {
        gson = new Gson();
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// JSON OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method encodes list of Code objects to json format and saves to a file.
     * @param codes the list of codes.
     * @param fileName the nam of the output file.
     * @throws JsonFileSavingException
     */

    public void encodeCodeList(List<Code> codes, String fileName) throws JsonFileSavingException {
        saveJsonToFile(gson.toJson(codes), fileName);
    }

    /**
     * This method decodes list of Code objects from a json file.
     * @param fileName the name of the json file.
     * @return the list of codes.
     */

    public List<Code> decodeCodeList(String fileName) {
        Type collectionType = new TypeToken<Collection<Code>>(){}.getType();
        String json = loadJsonFile(fileName);
        return json == null ?  null : (List<Code>) gson.fromJson(json, collectionType);
    }

    /**
     * This method saves json data to a file in the internal storage.
     * @param json the data to save.
     * @param fileName the file name to save to.
     * @throws JsonFileSavingException
     */

    private void saveJsonToFile(String json, String fileName) throws JsonFileSavingException {
        try(FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            outputStream.write(json.getBytes());
        } catch (Exception e) {
            throw new JsonFileSavingException(FILE_SAVING_ERROR);
        }
    }

    /**
     * This method loads json data from a file from the internal storage.
     * @param fileName the nam of the file.
     * @return
     */

    private String loadJsonFile(String fileName) {
        if (!jsonFileExists(fileName)) {
            return null;
        }

        try (Scanner reader = new Scanner(context.openFileInput(fileName))) {
            StringBuilder sb = new StringBuilder();
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method decides whether a json file exists or not.
     * @param fileName then nam of the file.
     * @return true if the file exists.
     */

    public boolean jsonFileExists(String fileName) {
        File file = new File(context.getFilesDir().getAbsolutePath() + "/" + fileName);
        return file.exists();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// JSON OPERATIONS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}

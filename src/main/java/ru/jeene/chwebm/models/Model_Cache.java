/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.models;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import lombok.Data;
import ru.jeene.chwebm.worker.utils.FileUtils;

/**
 *
 * @author anti1_000
 */
@Data
public class Model_Cache {

    HashMap<String, Model_Webm> list;

    public Model_Cache() {
        list = new HashMap<>();
    }

    public static Model_Cache load(String file, String codepage) {
        Model_Cache res;
        XStream xStream = new XStream(new DomDriver());
        String xml_file = FileUtils.readPage(file, codepage);
        res = (Model_Cache) xStream.fromXML(xml_file);
        return res;
    }

    public static void save(Model_Cache m, String file) throws FileNotFoundException, IOException {
        XStream xStream = new XStream(new DomDriver());
        FileOutputStream fs = new FileOutputStream(file);
        xStream.toXML(m, fs);
        fs.close();
    }

}

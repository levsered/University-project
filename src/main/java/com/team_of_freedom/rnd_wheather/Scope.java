package com.team_of_freedom.rnd_wheather;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;


import org.json.JSONObject;

@RestController
@RequestMapping("/scope")
public class Scope {
    @GetMapping
    public ScopeResponse showStatus() {
        db_work db = new db_work();
        float[] avg = {0, 1, 2};
        int our_el = 0;
        ArrayList<ArrayList<String>> base = db.read();
        String[] str = {"aa", "bb", "cc"};
        String tmp = "00";
        for(int i = 0; i < 8; i++)
        {
            String tmp2 = (new String(String.valueOf(base.get(i).get(0).charAt(11)))).concat(new String(String.valueOf(base.get(i).get(0).charAt(12))));
            if(tmp.equals(tmp2))
            {
                our_el = i;
                break;
            }
        }
        float avr_temp = 0;
        for(int k = 0; k < 3; k++)
        {
            for (int i = our_el; i < our_el + 8; i++)
            {
                avr_temp += (new Float(base.get(i).get(1))).floatValue();
                str[k] = base.get(i).get(2);
            }
            avg[k] = Math.round((avr_temp / 8) - 273.15f);
            our_el += 8;
            avr_temp = 0;
        }
//        db.parser();

        return new ScopeResponse("good", 1, avg, str);
    }

    private String GetString() {
        URLConnection connection = null;
        try {
            connection = new URL("https://api.openweathermap.org/data/2.5/forecast?id=501175&appid=e234bd4f6cf90feb586d37ac9c705a79").openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            assert connection != null;
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert is != null;
        InputStreamReader reader = new InputStreamReader(is);
        char[] buffer = new char[256];
        int rc;

        StringBuilder sb = new StringBuilder();

        while (true) {
            try {
                if ((rc = reader.read(buffer)) == -1) break;
                sb.append(buffer, 0, rc);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
